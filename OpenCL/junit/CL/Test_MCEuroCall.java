package CL;

import org.junit.Test;

public class Test_MCEuroCall {

	@Test
	public void test_GetPayoff() {
		
		float S = 152.35f;
		float K = 165f;
		float r = 0.0001f;
		float q = 0f;
		float sigma = 0.01f;
		int T = 252;
		float precision = 0.1f;
		float confidence = 0.96f;
		
		long startTime = System.currentTimeMillis();
		float price = MCEuroCall.pricing(S, K, r, q, sigma, T, precision, confidence);
		System.out.println("Simulation time is: " + (System.currentTimeMillis()-startTime)/1000. + " seconds.");
		System.out.println("European call option price is: " + price);
	}
}
