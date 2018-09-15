package MonteCarlo;

public interface Distribution {
	
	public double pdf(double x);	// pdf value at X=x
	public double cdf(double x);	// cdf value at X=x. The result is in [0, 1] 
	public double inv_cdf(double y);
	// returns the x s.t. CDF(x) = y. y is in [0, 1]
}
