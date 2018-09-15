package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

public class Test_EuropeanCall {

	@Test
	public void test_GetPayout() throws IOException {
		
		double S1 = 100;
		double drift1 = 0.0001;
		double sigma1 = 0.01;
		int T1 = 252;
		double K1 = 110;
		GaussianGenerator g1 = new GaussianGenerator();
		StockPath path1 = new GeometricPath(g1, S1, drift1, sigma1, T1);
		PayOut euroCall1 = new EuropeanCall(K1);
		
		PrintWriter w1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\EuropeanCall1.txt", "UTF-8");
		int simu1 = 10000;
		for (int i=0; i<simu1; i++) {
			w1.println(euroCall1.getPayout(path1));
		}
		w1.close();
	}
}
