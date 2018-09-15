package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

public class Test_GeometricPath {

	@Test
	public void test_GetPrices() throws IOException {
		
		// generate a path, graph it, and see if there is any weird behavior
		double S1 = 100;
		double drift1 = 0.0001;
		double std1 = 0.01;
		int T1 = 252;
		GeometricPath GBM1 = new GeometricPath(S1, drift1, std1, T1);
		List<Double> GBM_Path1 = GBM1.getPrices();
		
		PrintWriter w1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\GBM1.txt", "UTF-8");
		for (int i=0; i<T1; i++) {
			w1.println(GBM_Path1.get(i));
		}
		w1.close();
		
		// generate many paths, take their ending value, then:
		// 1) the ending values should have lognormal distribution
		// 2) the ending values should have mean S * exp(rT)
		double S2 = 100;
		double drift2 = 0.0001;
		double std2 = 0.01;
		int T2 = 252;
		GeometricPath GBM2 = new GeometricPath(S2, drift2, std2, T2);
		
		PrintWriter w2 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\GBM2.txt", "UTF-8");
		
		int simu2 = 50000;
		double[] res2 = new double[simu2];
		for (int i=0; i<simu2; i++) {
			res2[i] = GBM2.getPrices().get(T2-1);
			w2.println(res2[i]);
		}
		w2.close();
	}

}
