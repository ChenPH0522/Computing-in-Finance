package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class Test_LogNormalGenerator {

	@Test
	public void test_GetVector() throws IOException {
		
		// Use graphs in python or excel to see whether the vectors are Gaussion
		
		int n1 = 10000;
		double mu = 5;
		double sigma = 0.5;
		LogNormalGenerator g1 = new LogNormalGenerator(mu, sigma);
		double[] lognormal1 = g1.getVector(n1);
		
		PrintWriter w1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\LogNormal1.txt", "UTF-8");
		for (int i=0; i<n1; i++) {
			w1.println(lognormal1[i]);
		}
		w1.close();
	}
}
