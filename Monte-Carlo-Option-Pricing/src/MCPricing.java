package MonteCarlo;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Penghao Chen
 * Monte Carlo Option Pricing implementation
 */

public class MCPricing {
	
	private static final int _MAXLOOP = 1000000;	//maximum number of loops
	private static final int _MINLOOP = 10000;		//minimum number of loops
	private static final Distribution gauss = new GaussianDistribution();
	
	/**
	 * 
	 * @param path		stock path. Can be ArithmeticPath, GeometricPath or other
	 * @param payOut	PayOut type. Can be European Call, Asian Call or other
	 * @param discount	Discount rate for each time interval, on a continuous basis.
	 * @param precision	The desired level of difference between the simulated price
	 * and the hypothetical true value  
	 * @param confidence	The confidence level of the simulated price. 
	 * @return	simulated price of the option
	 * Note: this method requires a minimum of 10000 simulations,
	 * and a maximum 1000000 simulations
	 */
	public static double Pricing(StockPath path, PayOut payOut, double discount,
			double precision, double confidence) {
		
		//long startTime = System.currentTimeMillis();
		
		double criticalValue = gauss.inv_cdf(0.5 + confidence/2);
		
		double n = 1;
		double payoffPV = 0;
		double sampleVar = 0;
		double sampleMean = 0;
		double old_Mean = 0;
		double threshold = Double.MAX_VALUE;
		
		while ((precision < threshold || n < _MINLOOP) && (n <_MAXLOOP)) {
			
			payoffPV = payOut.getPayout(path) * Math.exp(-discount*path.getPeriod());
			
			sampleMean = old_Mean + (payoffPV - old_Mean)/n; 
			if (n > 1) {
				sampleVar = (n-2)/(n-1)*sampleVar + 
						n*(sampleMean-old_Mean)*(sampleMean-old_Mean);
			}
			
			old_Mean = sampleMean;
			threshold = criticalValue * Math.sqrt(sampleVar/n);
			n += 1;			
		}
		
		if (n == _MAXLOOP) {
			System.out.println("Max number of iterations reached "
					+ "before reaching specified confidence level.");
			return -1;
		}
		
		//long elapsedTime = System.currentTimeMillis() - startTime;
		//System.out.println("Elapsed time: " + elapsedTime);
		//System.out.println("Number of iterations: " + (int) n);
		//System.out.println("Sample Variance: " + sampleVar);
		return sampleMean;
	}
	
	public static void main(String[] args) throws IOException {
		
		int MC_n = 100;
		double S = 152.35;
		double K = 165;
		double r = 0.0001;
		double sigma = 0.01;
		int T = 252;
		
		StockPath GBM = new GeometricPath(S, r, sigma, T);
		PayOut EuroCall = new EuropeanCall(K);
		double precision = 0.1;
		double confidence = 0.96;
		
		PrintWriter w1 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\MC_EuroCall.txt", "UTF-8");
		for (int i=0; i<MC_n; i++) {
			w1.println(MCPricing.Pricing(GBM, EuroCall, r, precision, confidence));
		}
		w1.close();
		
		double K2 = 164;
		PayOut AsianCall = new AsianCall(K2);
		PrintWriter w2 = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MONTECARLO HW\\MC_AsianCall.txt", "UTF-8");
		for (int i=0; i<MC_n; i++) {
			w2.println(MCPricing.Pricing(GBM, AsianCall, r, precision, confidence));
		}
		w2.close();
	}
}
