package Middleware;

import javax.jms.JMSException;
import org.junit.Test;

public class Test_SimuManager_ResultListener {

	@Test
	public void test() throws JMSException, InterruptedException {
		
		String option1 = "European Call";
		double S1 = 152.35;
		double K1 = 165;
		double r1 = 0.0001;
		double sigma1 = 0.01;
		double q1 = 0;
		int T1 = 252;
		double error1 = 0.1;
		double confidence1 = 0.96;
		
		SimuManager euroCall1 = new SimuManager("EuroCallRequest", "EuroCallData", 100);
		euroCall1.MCPricing(option1, S1, K1, r1, sigma1, q1, T1, error1, confidence1);
		// 100 requests should be generated, and this thread waits for results
		// Check over the ActiveMQ control platform - Good!
		// One topic is generated -- EuroCallRequest
		// One queue is generated -- EuroCallData
	}

}
