package MonteCarlo;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_AntitheticGenerator {

	@Test
	public void test_GetPrices() {
		
		RandomVectorGenerator g1 = new GaussianGenerator();
		RandomVectorGenerator ag1 = new AntitheticGenerator(g1);
		int n1 = 10;
		double[] vec1 = ag1.getVector(n1);
		double[] vec2 = ag1.getVector(n1);
		double[] res1 = new double[n1];
		double[] compare1 = new double[n1];
		for (int i=0; i<n1; i++) {
			res1[i] = vec1[i] + vec2[i];
		}
		assertArrayEquals(compare1, res1, 0);
	}
}
