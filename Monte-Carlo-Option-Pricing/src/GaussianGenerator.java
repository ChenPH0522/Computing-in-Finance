package MonteCarlo;

import java.util.Random;

public class GaussianGenerator implements RandomVectorGenerator {

	private double _mu = 0;
	private double _sigma = 1;
	
	//getters & setters
	public double getMu() {return _mu;}
	public double getSigma() {return _sigma;}
	public void setMu(double new_Mu) {_mu = new_Mu;}
	public void setSigma(double new_Sigma) {_sigma = new_Sigma;}
	
	//constructor
	public GaussianGenerator() {}
	/**
	 * @param mu		mean
	 * @param sigma		standard deviation
	 */
	public GaussianGenerator(double mu, double sigma) {
		_mu = mu;
		_sigma = sigma;
	}
	
	/**
	 * 
	 * @param n		length of the vector
	 * @return		a vector with each element independently drawn from 
	 * a Gaussian distribution with specified mean and standard deviation.
	 */
	@Override
	public double[] getVector(int n) {
		
		Random r = new Random();
		double[] res = new double[n];
		
		for (int i=0; i<n; i++) {
			res[i] = r.nextGaussian() * _sigma + _mu;
		}
		return res;
	}
}
