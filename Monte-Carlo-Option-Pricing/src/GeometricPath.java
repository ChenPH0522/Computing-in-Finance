package MonteCarlo;

import java.util.Arrays;
import java.util.List;

/**
 * @author Penghao Chen
 * This class defines a process whose continuous growth rate follows a pattern.
 * The value at time t is defined as: (value at t-1) * exp(some pattern) 
 */
public class GeometricPath implements StockPath {
	
	private RandomVectorGenerator _generator = new AntitheticGenerator(new GaussianGenerator());
	private double _S = 100;
	private double _drift = 0;	// drift PER TIME INTERVAL
	private double _sigma = 1;	// standard diviation PER TIME INTERVAL
	private int _T = 252;			// number of periods
	
	//getters
	public RandomVectorGenerator getGenerator() {return _generator;}
	public double getStartingPrice() {return _S;}
	public double getDrift() {return _drift;}
	public double getSigma() {return _sigma;}
	@Override
	public int getPeriod() {return _T;}
	
	//setters
	public void setGenerator(RandomVectorGenerator generator) {_generator = generator;}
	public void setStartingPrice(double S) {_S = S;}
	public void setDrift(double drift) {_drift = drift;}
	public void setSigma(double sigma) {_sigma = sigma;}
	public void setT(int T) {_T = T;}
	
	//constructor
	public GeometricPath() {}
	/**
	 * @param generator		generator that generates price changes for each period
	 */
	public GeometricPath(RandomVectorGenerator generator) {_generator = generator;}
	/**
	 * @param S				starting price
	 * @param drift			stock drift for each period. Under risk-neutral assumption,
	 * this should be the risk-free rate. If the stock has dividends,
	 * simply put (expected drift - dividend yield) here 
	 * @param sigma			stock volatility for each period.
	 * @param T				number of time periods
	 */
	public GeometricPath(double S, double drift, double sigma, int T) {
		_S = S;
		_drift = drift;
		_sigma = sigma;
		_T = T;
	}
	/**
	 * @param generator		generator that generates prices changes for each period
	 * @param S				starting price
	 * @param drift			stock drift for each period. Under risk-neutral assumption,
	 * this should be the risk-free rate. If the stock has dividends,
	 * simply put (expected drift - dividend yield) here 
	 * @param sigma			stock volatility for each period.
	 * @param T				number of time periods
	 */
	public GeometricPath(
			RandomVectorGenerator generator, double S, double drift, double sigma, int T) {
		_generator = generator;
		_S = S;
		_drift = drift;
		_sigma = sigma;
		_T = T;
	}
	
	/**
	 * @return		path of a movement with length equal to number of periods 
	 */
	@Override
	public List<Double> getPrices() {
		return getPrices(_T);
	}
	
	/**
	 * 
	 * @param n		length of the path, including the starting point
	 * @return		path of a movement with length n
	 */
	public List<Double> getPrices(int n) {

		List<Double> res = Arrays.asList(new Double[n]);
		res.set(0, _S);
		double[] changes = _generator.getVector(n-1);
		for (int i=1; i<n; i++) {
			res.set(i, res.get(i-1)*Math.exp(
					_drift - 0.5*_sigma*_sigma + _sigma*changes[i-1]));
		}
		return res;
	}
}
