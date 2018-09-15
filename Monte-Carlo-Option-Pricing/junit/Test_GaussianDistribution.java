package MonteCarlo;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_GaussianDistribution {

	@Test
	public void test_GaussianDistribution() {
		
		// standard normal
		GaussianDistribution gauss1 = new GaussianDistribution();
		assertEquals(0.39894228, gauss1.pdf(0), 0.0001);
		assertEquals(0.975002105, gauss1.cdf(1.96), 0.0001);
		assertEquals(1.281551566, gauss1.inv_cdf(0.9), 0.0001);
		assertEquals(2.326347874, gauss1.inv_cdf(0.99), 0.0001);
		
		// normal with other mean and std
		double mean2 = 5;
		double std2 = 2;
		GaussianDistribution gauss2 = new GaussianDistribution(mean2, std2);
		assertEquals(0.064758798, gauss2.pdf(2), 0.0001);
		assertEquals(0.691462461, gauss2.cdf(6), 0.0001);
		assertEquals(7.563103131, gauss2.inv_cdf(0.9), 0.0001);
		assertEquals(0.347304252, gauss2.inv_cdf(0.01), 0.0001);
	}

}
