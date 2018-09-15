package MonteCarlo;

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

public class GaussianDistribution implements Distribution{
	
	private double _mu = 0;
	private double _sigma = 1;
	
	private final double _A0 = 0.2316419;
	private final double _A1 = 0.31938153;
	private final double _A2 = -0.356563782;
	private final double _A3 = 1.781477937;
	private final double _A4 = -1.821255978;
	private final double _A5 = 1.330274429;
	
	private final double _a1 = 2.50662823884;
	private final double _a2 = -18.61500062529;
	private final double _a3 = 41.39119773534;
	private final double _a4 = -25.44106049637;

	private final double _b1 = -8.47351093090;
	private final double _b2 = 23.08336743743;
	private final double _b3 = -21.06224101826;
	private final double _b4 = 3.13082909833;

	private final double _c1 = 0.3374754822726147;
	private final double _c2 = 0.9761690190917186;
	private final double _c3 = 0.1607979714918209;
	private final double _c4 = 0.0276438810333863;
	private final double _c5 = 0.0038405729373609;
	private final double _c6= 0.0003951896511919;
	private final double _c7 = 0.0000321767881768;
	private final double _c8 = 0.0000002888167364;
	private final double _c9 = 0.0000003960315187;
	
	//getter and setter
	public double getMu() {return _mu;}
	public double getSigma() {return _sigma;}
	
	//constructor
	public GaussianDistribution() {}
	public GaussianDistribution(double mu, double sigma) {
		_mu = mu; _sigma = sigma;
	}
	
	/**
	 * @return returns the pdf value at X=x
	 */
	@Override
	public double pdf(double x) {
		return Math.exp(-1 * (x-_mu)*(x-_mu) / 2 / (_sigma*_sigma)) / 
				Math.sqrt(Math.PI * 2) / _sigma;
	}

	/**
	 * @return returns the cdf value at X=x
	 */
	@Override
	public double cdf(double x) {
		
		GaussianDistribution g = new GaussianDistribution();
		double norm_x = (x - _mu)/_sigma;
		double t;
		double p;
		
		if (norm_x > 0) {
			t = 1/(1 + _A0*norm_x);
			p = g.pdf(norm_x);
			return 1-p*(_A1*t + 
						_A2*t*t + 
						_A3*t*t*t +
						_A4*t*t*t*t +
						_A5*t*t*t*t*t);
		}
		
		norm_x = -norm_x;
		t = 1/(1 + _A0*norm_x);
		p = g.pdf(norm_x);
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
	@Override
	public double inv_cdf(double x) {

		if (x>0.08 && x<0.92) {
			double Y = x-0.5;
			double Z = Y*Y;
			return Y*(_a1 + _a2*Z + _a3*Z*Z + _a4*Z*Z*Z) / 
					(1 + _b1*Z + _b2*Z*Z +_b3*Z*Z*Z + _b4*Z*Z*Z*Z)*_sigma + _mu;
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
					_c9*K*K*K*K*K*K*K*K) * _sigma + _mu;
		}
		
		Z = 1-x;
		K = Math.log(-Math.log(Z));
		return (_c1 + _c2*K + _c3*K*K + _c4*K*K*K +
				_c5*K*K*K*K +
				_c6*K*K*K*K*K +
				_c7*K*K*K*K*K*K +
				_c8*K*K*K*K*K*K*K +
				_c9*K*K*K*K*K*K*K*K) * _sigma + _mu;
	}
}
