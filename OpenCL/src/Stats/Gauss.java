package Stats;

/**
 * @author Penghao Chen
 * This distribution contains methods giving the Gaussian pdf at x,
 * the Gaussian CDF at x, and the inverse Gaussian CDF at y (y in [0,1]).
 * 
 * Ideally, this class should contain all information related to Gaussian distribution,
 * suggesting one further developing goal is to include different moments
 * of the Gaussian distribution.
 * 
 * The numerical methods can be referenced here:
 * 		http://www.quantopia.net/cumulative-normal-distribution/
 * 		http://www.quantopia.net/inverse-normal-cdf/
 */

public class Gauss{

	private static final double _A0 = 0.2316419;
	private static final double _A1 = 0.31938153;
	private static final double _A2 = -0.356563782;
	private static final double _A3 = 1.781477937;
	private static final double _A4 = -1.821255978;
	private static final double _A5 = 1.330274429;
	
	private static final double _a1 = 2.50662823884;
	private static final double _a2 = -18.61500062529;
	private static final double _a3 = 41.39119773534;
	private static final double _a4 = -25.44106049637;

	private static final double _b1 = -8.47351093090;
	private static final double _b2 = 23.08336743743;
	private static final double _b3 = -21.06224101826;
	private static final double _b4 = 3.13082909833;

	private static final double _c1 = 0.3374754822726147;
	private static final double _c2 = 0.9761690190917186;
	private static final double _c3 = 0.1607979714918209;
	private static final double _c4 = 0.0276438810333863;
	private static final double _c5 = 0.0038405729373609;
	private static final double _c6= 0.0003951896511919;
	private static final double _c7 = 0.0000321767881768;
	private static final double _c8 = 0.0000002888167364;
	private static final double _c9 = 0.0000003960315187;
	
	/**
	 * @return returns the pdf value at X=x
	 */
	public static double pdf(double x, double mu, double sigma) {
		return Math.exp(-1 * (x- mu)*(x- mu) / 2 / (sigma*sigma)) / 
				Math.sqrt(Math.PI * 2) / sigma;
	}

	/**
	 * @return returns the cdf value at X=x
	 */
	public static double cdf(double x, double mu, double sigma) {
		
		double norm_x = (x - mu)/sigma;
		double t;
		double p;
		
		if (norm_x > 0) {
			t = 1/(1 + _A0*norm_x);
			p = pdf(norm_x, 0, 1);
			return 1-p*(_A1*t + 
						_A2*t*t + 
						_A3*t*t*t +
						_A4*t*t*t*t +
						_A5*t*t*t*t*t);
		}
		
		norm_x = -norm_x;
		t = 1/(1 + _A0*norm_x);
		p = pdf(norm_x, 0, 1);
		return 1-(1-p*(_A1*t + 
				_A2*t*t + 
				_A3*t*t*t +
				_A4*t*t*t*t +
				_A5*t*t*t*t*t));		
	}

	/**
	 * @return returns the inverse cdf value y at X=x,
	 * such that GaussianDistribution.cdf(y) = x 
	 */
	public static double inv_cdf(double x, double mu, double sigma) {

		if (x>0.08 && x<0.92) {
			double Y = x-0.5;
			double Z = Y*Y;
			return Y*(_a1 + _a2*Z + _a3*Z*Z + _a4*Z*Z*Z) / 
					(1 + _b1*Z + _b2*Z*Z +_b3*Z*Z*Z + _b4*Z*Z*Z*Z)*sigma + mu;
		}
		
		double Y = x-0.5;
		double Z;
		double K;
		if (Y <= 0) {
			Z = x;
			K = Math.log(-Math.log(Z));
			return -1*(_c1 + _c2*K + _c3*K*K + _c4*K*K*K +
					_c5*K*K*K*K +
					_c6*K*K*K*K*K +
					_c7*K*K*K*K*K*K +
					_c8*K*K*K*K*K*K*K +
					_c9*K*K*K*K*K*K*K*K) * sigma + mu;
		}
		
		Z = 1-x;
		K = Math.log(-Math.log(Z));
		return (_c1 + _c2*K + _c3*K*K + _c4*K*K*K +
				_c5*K*K*K*K +
				_c6*K*K*K*K*K +
				_c7*K*K*K*K*K*K +
				_c8*K*K*K*K*K*K*K +
				_c9*K*K*K*K*K*K*K*K) * sigma + mu;
	}
}
