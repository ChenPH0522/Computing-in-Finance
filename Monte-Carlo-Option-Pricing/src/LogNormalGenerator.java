package MonteCarlo;

public class LogNormalGenerator implements RandomVectorGenerator {

	private GaussianGenerator _generator = new GaussianGenerator();

	//getter and setter
	public GaussianGenerator getGenerator() {return _generator;}
	public void setGenerator(GaussianGenerator generator) {_generator = generator;}

	//Constructor
	public LogNormalGenerator() {}
	public LogNormalGenerator(GaussianGenerator generator) {_generator = generator;}
	public LogNormalGenerator(double mu, double sigma) {
		_generator = new GaussianGenerator(mu, sigma);
	}
	
	/**
	 * @param n		length of the vector
	 * @return		a vector with each element independently drawn from 
	 * a lognormal distribution. The lognormal distribution is based on a 
	 * Gaussian distribution with specified mean and standard deviation.
	 */
	@Override
	public double[] getVector(int n) {

		double[] gaussianVec = _generator.getVector(n);
		double[] res = new double[n];
		
		for (int i=0; i<n; i++) {
			res[i] = Math.exp(gaussianVec[i]);
		}
		return res;
	}
}
