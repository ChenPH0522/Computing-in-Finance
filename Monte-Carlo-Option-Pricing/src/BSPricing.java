package MonteCarlo;

/**
 * @author Penghao Chen
 * This class is solely devoted to using Black-Scholes formulat to price European Call
 * and European Put options, since they are the only ones with rigorous analytical
 * solution - Black-Scholes formula.
 */
public class BSPricing {
	
	private static final Distribution gauss = new GaussianDistribution();
	
	/**
	 * Input r, sigma, and q are on a continuous basis.
	 * @param S		starting price.
	 * @param K		striking price.
	 * @param r		drift within each time interval. Usually risk-free rate.
	 * @param sigma	price return volatility within each time interval.
	 * @param T		number of periods before expiration.
	 * @param q		divident yield within each time interval.
	 * @return		Price of an European Call option.
	 */
	public static double PricingCall(double S, double K, double r, 
			double sigma, int T, double q) {
		
		double d1 = (Math.log(S/K) + (r-q + sigma*sigma/2) * T) / (sigma * Math.sqrt(T));
		double d2 = d1 - sigma * Math.sqrt(T);
		double N_d1 = gauss.cdf(d1);
		double N_d2 = gauss.cdf(d2);
		return S*Math.exp(-q*T)*N_d1 - K*Math.exp(-r*T)*N_d2;
	}
	
	/**
	 * Input r, sigma, and q are on a continuous basis.
	 * @param S		starting price.
	 * @param K		striking price.
	 * @param r		drift within each time interval. Usually risk-free rate.
	 * @param sigma	price return volatility within each time interval.
	 * @param T		number of periods before expiration.
	 * @param q		divident yield within each time interval.
	 * @return		Price of an European Put option.
	 */
	public static double PricingPut(double S, double K, double r, 
			double sigma, int T, double q) {
		
		double d1 = (Math.log(S/K) + (r-q + sigma*sigma/2) * T) / (sigma * Math.sqrt(T));
		double d2 = d1 - sigma * Math.sqrt(T);
		double N_d1 = gauss.cdf(d1);
		double N_d2 = gauss.cdf(d2);
		
		return K*Math.exp(-r*T)*(1-N_d2) - S*Math.exp(-q*T)*(1-N_d1); 
	}
}
