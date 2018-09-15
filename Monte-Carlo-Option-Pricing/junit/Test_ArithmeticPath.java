package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

public class Test_ArithmeticPath {

	@Test
	public void test_getPrices() throws IOException {

		// Use graphs in excel to see whether the vectors are Gaussion
		
		// standard normal increment
		GaussianGenerator g1 = new GaussianGenerator(0, 1);
		double S1 = 100;
		int T1 = 10000;
		ArithmeticPath BM1 = new ArithmeticPath(g1, S1, T1);
		List<Double> path1 = BM1.getPrices();
		
		PrintWriter writer1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\BM1.txt", "UTF-8");
		for (int i=0; i<T1; i++) {
			writer1.println(path1.get(i));
		}
		writer1.close();
		
		// normal increment with (0.1, 4)
		double mean2 = 0.1;
		double std2 = 4;
		GaussianGenerator g2 = new GaussianGenerator(mean2, std2);;
		double S2 = 100;
		int T2 = 10000;
		ArithmeticPath BM2 = new ArithmeticPath(g2, S2, T2);
		List<Double> path2 = BM2.getPrices();
		
		PrintWriter writer2 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\BM2.txt", "UTF-8");
		for (int i=0; i<T2; i++) {
			writer2.println(path2.get(i));
		}
		writer2.close();
	}

}
