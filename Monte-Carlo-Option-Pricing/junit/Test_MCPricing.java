package MonteCarlo;

import org.junit.Test;

public class Test_MCPricing {

	@Test
	public void test_Pricing() {
		
		// European call option
		double S1 = 152.35;
		double K1 = 165;
		double r1 = 0.0001;
		double sigma1 = 0.01;
		int T1 = 252;
		
		GaussianGenerator g1 = new GaussianGenerator();
		StockPath GBM1 = new GeometricPath(S1, r1, sigma1, T1);
		((GeometricPath) GBM1).setGenerator(g1);
		PayOut euroCall1 = new EuropeanCall(K1);
		
		double error1 = 0.1;
		double confidence1 = 0.96;
		System.out.println(MCPricing.Pricing(GBM1, euroCall1, r1, error1, confidence1));
		System.out.println(BSPricing.PricingCall(S1, K1, r1, sigma1, T1, 0));
		//The code works, but the result is unstable
		//When strike price is high, Monte Carlo Pricing is sensitive to simulated paths
		//because payoffs are dominantly 0's
		
		// Asian call optiond
		double S2 = 152.35;
		double K2 = 164;
		double r2 = 0.0001;
		double sigma2 = 0.01;
		int T2 = 252;
		
		StockPath GBM2 = new GeometricPath(S2, r2, sigma2, T2);
		
		PayOut asianCall = new AsianCall(K2);
		
		double error2 = 0.1;
		double confidence2 = 0.96;
		System.out.println(MCPricing.Pricing(GBM2, asianCall, r2, error2, confidence2));
		//The code works, and the result confirms with our intuitive:
		//Asian Calls are cheaper than European Calls, because the volatility
		//of the average price is lower than the volatility of the ending price
	}

}
