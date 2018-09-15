package MonteCarlo;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_BSPricing {

	@Test
	public void test_PricingCall() {
		
		//The test would fail if the precision is raised from 0.01 to 0.001
		//There's a difference in numerical methods between Excel and Java
		//Prices calculated using this Java program is consistently slightly higher than 
		//those calucluated by Excel
		
		// European Call 1
		double S1 = 100;
		double K1 = 110;
		double r1 = 0.0001;
		double sigma1 = 0.01;
		double q1 = 0;
		int T1 = 252;
		
		assertEquals(3.552353301, BSPricing.PricingCall(S1, K1, r1, sigma1, T1, q1), 0.01);
		
		//European Call 2
		double S2 = 50;
		double K2 = 55;
		double r2 = 0.0001;
		double sigma2 = 0.01;
		double q2 = 0;
		int T2 = 252;
		assertEquals(1.776176651, BSPricing.PricingCall(S2, K2, r2, sigma2, T2, q2), 0.01);
	}

	@Test
	public void test_PricingPut() {
		
		//Put pricing is more precise than Call pricing for the specified inputs.
		//The differences are smaller than 10^-5.
		//There's a difference in numerical methods between Excel and Java
		//Prices calculated using this Java program is consistently slightly lower than 
		//those calucluated by Excel
		
		// European put 1
		double S1 = 100;
		double K1 = 90;
		double r1 = 0.0001;
		double sigma1 = 0.01;
		double q1 = 0;
		int T1 = 252;
		
		assertEquals(1.714758491, BSPricing.PricingPut(S1, K1, r1, sigma1, T1, q1), 0.01);
		
		//European put 2
		double S2 = 50;
		double K2 = 45;
		double r2 = 0.0001;
		double sigma2 = 0.01;
		double q2 = 0;
		int T2 = 252;
		assertEquals(0.857379246, BSPricing.PricingPut(S2, K2, r2, sigma2, T2, q2), 0.01);
		
	}
}
