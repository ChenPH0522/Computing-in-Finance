package Middleware;

import java.util.HashMap;
import java.util.LinkedList;

import javax.jms.*;

import org.apache.activemq.*;
import MonteCarlo.*;

/**
 * 
 * @author Penghao Chen
 * SimuSlave simulates the stock price path and returns the final payoff according to specified 
 * PayOut function.
 */
public class SimuSlave {

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
	protected static HashMap<String, PayOut> _optionTypes = new HashMap<>();
		static {
			_optionTypes.put("European Call", new EuropeanCall());
			_optionTypes.put("Asian Call", new AsianCall());
		}
	protected GeometricPath _path = new GeometricPath();
	protected LinkedList<String[]> _requestList = new LinkedList<>();
	protected boolean _isWaiting = true;
	
	// constructor
	public SimuSlave(String sendQueue, String receiveTopic) throws JMSException {
		_connection = _factory.createConnection();
		_connection.start();
		_session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		_sendQueue = _session.createQueue(sendQueue);
		_receiveQueue = _session.createTopic(receiveTopic);
		_producer = _session.createProducer(_sendQueue);
		_consumer = _session.createConsumer(_receiveQueue);
		_consumer.setMessageListener(new RequestListener(this));
	}	
	
	/**
	 * Listens to requests and generate payoffs.
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	public void generate() throws JMSException, InterruptedException {
		
		PayOut payout;
		int loop;

		while (true) {
			
			// waiting for requests
			while (_isWaiting) {Thread.sleep(1);}
			
			synchronized(_requestList) {
				while (_requestList.size()>0) {
					String[] msg = _requestList.poll();
					loop = Integer.parseInt(msg[0]);
					payout = SimuSlave._optionTypes.get(msg[1]);
					payout.setStrike(Double.parseDouble(msg[2]));
					_path.setStartingPrice(Double.parseDouble(msg[3]));
					_path.setDrift(Double.parseDouble(msg[4]));
					_path.setSigma(Double.parseDouble(msg[5]));
					_path.setT(Integer.parseInt(msg[6]));
					
					StreamMessage result = _session.createStreamMessage();
					for (int i=0; i<loop; i++) {
						result.writeDouble(payout.getPayout(_path));
					}
					_producer.send(result);
				}
				_isWaiting = true;
			}
		}
	}
	
	/**
	 * Close the connection.
	 * @throws JMSException
	 */
	public void close() throws JMSException {
		if (_connection != null) {_connection.close();}
	}
	
	public static void main(String[] args) throws JMSException, InterruptedException {
		SimuSlave slave1 = new SimuSlave("EuroCallData", "EuroCallRequest");
		slave1.generate();
	}

}
