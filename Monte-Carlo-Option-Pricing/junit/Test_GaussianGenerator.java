package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Test;

public class Test_GaussianGenerator {

	@Test
	public void test_GetVector() throws IOException {
		
		// Use graphs in python or excel to see whether the vectors are Gaussion
		
		// standard normal
		int n1 = 10000;
		GaussianGenerator g1 = new GaussianGenerator();
		double[] G_vector1 = g1.getVector(10000);
		
		PrintWriter writer1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\Gaussian1.txt", "UTF-8");
		for (int i=0; i<n1; i++) {
			writer1.println(G_vector1[i]);
		}
		writer1.close();
		
		// normal with (mean, std) other than (0, 1)
		int n2 = 100000;
		int mean2 = 10;
		int std2 = 10;
		GaussianGenerator g2 = new GaussianGenerator(mean2, std2);
		double[] G_vector2 = g2.getVector(n2);
		PrintWriter writer2 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\Gaussian2.txt", "UTF-8");
		for (int i=0; i<n1; i++) {
			writer2.println(G_vector2[i]);
		}
		writer2.close();
	}

}
