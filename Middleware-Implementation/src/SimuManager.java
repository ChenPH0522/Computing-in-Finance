package Middleware;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.jms.*;

import org.apache.activemq.*;

import MonteCarlo.*;

public class SimuManager {
	
	// connection settings
	protected static String _brokerURL = "tcp://localhost:61616";
	protected static ActiveMQConnectionFactory _factory = new ActiveMQConnectionFactory(_brokerURL);
	protected Connection _connection;
	protected Session _session;
	protected Destination _sendQueue;
	protected Destination _receiveQueue;
	protected MessageProducer _producer;
	protected MessageConsumer _consumer;
	
	// simulation settings
	protected int _simuPackageSize;
	protected double[] _simuResults;
	protected static final int _MINLOOP = 10000;		//minimum number of loops
	protected static final Distribution gauss = new GaussianDistribution();
	protected boolean _isWaiting = true;
	
	// constructor
	/**
	 * @param sendTopic		Name of the topic through which requests are published.
	 * @param receiveQueue	Name of the queue through which simulation results are sent back.
	 * @param simuPackageSize	How many simulations requests to generate at one time.
	 * @throws JMSException
	 */
	public SimuManager(String sendTopic, String receiveQueue, int simuPackageSize) 
			throws JMSException {
		_connection = _factory.createConnection();
		_connection.start();
		_session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		_sendQueue = _session.createTopic(sendTopic);
		_receiveQueue = _session.createQueue(receiveQueue);
		_producer = _session.createProducer(_sendQueue);
		_consumer = _session.createConsumer(_receiveQueue);
		ResultListener a = new ResultListener(this);
		_consumer.setMessageListener(a);
		_simuPackageSize = simuPackageSize;
		_simuResults = new double[_simuPackageSize];
	}

	/**
	 * @param optionType	String. Use the pre-defined list of option names.
	 * @param S0			Starting price.
	 * @param K				Striking price.
	 * @param r				Risk-free rate.
	 * @param sigma			Volatility.
	 * @param q				Dividend yield.
	 * @param T				Number of periods.
	 * @param precision		User specified maximum error between the esimate and the theoretical true value. 
	 * @param confidence	Statistical confidence level.
	 * @return	Estimated option price.
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	public double MCPricing(String optionType, double S0, double K, double r, double sigma, 
			double q, int T, double precision, double confidence)
			throws JMSException, InterruptedException {
		
		double criticalValue = gauss.inv_cdf(0.5 + confidence/2);
		double n = 1;
		double payoffPV = 0;
		double sampleVar = 0;
		double sampleMean = 0;
		double old_Mean = 0;
		double threshold = Double.MAX_VALUE;
		
		while (precision < threshold || n < _MINLOOP) {
			
			// if criteria not met: send more requests
			TextMessage msg = _session.createTextMessage(
					_simuPackageSize + ","
					+ optionType + ","
					+ K + ","
					+ S0 + ","
					+ (r-q) + ","
					+ sigma + ","
					+ T + ",");
			_producer.send(msg);
			_isWaiting = true;
			
			// wait for listener to collect results
			while (_isWaiting) {Thread.sleep(1);}
			
			// update price
			synchronized(_simuResults) {
				for (int i=0; i<_simuPackageSize; i++) {
					
					payoffPV = _simuResults[i] * Math.exp(-r*T);
					sampleMean = old_Mean + (payoffPV - old_Mean)/n; 
					if (n > 1) {
						sampleVar = (n-2)/(n-1)*sampleVar + 
								n*(sampleMean-old_Mean)*(sampleMean-old_Mean);
					}
					
					old_Mean = sampleMean;
					threshold = criticalValue * Math.sqrt(sampleVar/n);
					n += 1;
				}
			}
		}
		
		return sampleMean;
	}
	
	/**
	 * Close the connection.
	 * @throws JMSException
	 */
	public void close() throws JMSException {
		if (_connection != null) {
			_connection.close();
		}
	}
	
	public static void main(String[] args) throws JMSException, InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		//long t1 = System.currentTimeMillis();
		
		String option1 = "European Call";
		double S1 = 152.35;
		double K1 = 165;
		double r1 = 0.0001;
		double sigma1 = 0.01;
		double q1 = 0;
		int T1 = 252;
		double error1 = 0.1;
		double confidence1 = 0.96;
		int packageSize1 = 100;

		SimuManager euroCall1 = new SimuManager("EuroCallRequest", "EuroCallData", packageSize1);

		double price = euroCall1.MCPricing(option1, S1, K1, r1, sigma1, q1, T1, error1, confidence1);
		
		System.out.println(price);
		//System.out.println(System.currentTimeMillis() - t1);
		euroCall1.close();
	}
}
