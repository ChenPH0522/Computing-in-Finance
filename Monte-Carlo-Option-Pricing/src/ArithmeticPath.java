package MonteCarlo;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Penghao Chen
 * This class represents a process similar to random walk. Increment of the process
 * are identically, independently distributed. 
 * The value at time t is definded as: starting value + sum(all increments up to t).
 * Random Walk, Arithmetic Brownian Motion, are special cases of such process.
 */

public class ArithmeticPath implements StockPath {

	private RandomVectorGenerator _generator = new AntitheticGenerator(new GaussianGenerator());
	private double _S = 100;		//starting price
	private int _T = 1;
	
	//getters
	public RandomVectorGenerator getGenerator() {return _generator;}
	public double getStartingPrice() {return _S;}
	@Override
	public int getPeriod() {return _T;}
	
	//setters
	public void setGenerator(RandomVectorGenerator generator) {_generator = generator;}
	public void setStartingPrice(double S) {_S = S;}
	public void setT(int T) {_T = T;}
	
	//constructor
	public ArithmeticPath() {}
	/**
	 * @param S		starting price. Default value is 100.
	 * @param T		number of periods Defualt value is 1.
	 */
	public ArithmeticPath(double S, int T) {_S = S; _T = T;}
	/**
	 * @param generator		the underlying RandomVectorGenerator based on which
	 * the whole process(path) is generated
	 */
	public ArithmeticPath(RandomVectorGenerator generator) {_generator = generator;}
	/**
	 * @param generator		the underlying RandomVectorGenerator based on which
	 * the whole process(path) is generated
	 * @param S		starting price. Defaut value is 100.
	 * @param T		number of periods. Default value is 1.
	 */
	public ArithmeticPath(RandomVectorGenerator generator, double S, int T) {
		_generator = generator; 
		_S = S;
		_T = T;
	}
	
	/**
	 * Note that the method assuems a positive process.
	 * @return		path of a movement with length equal to number of periods
	 */
	@Override
	public List<Double> getPrices() {
		return getPrices(_T);
	}
	
	/**
	 * Note that the method assuems a positive process.
	 * @param n		length of the path, including the starting point
	 * @return stock path with length n
	 */
	public List<Double> getPrices(int n) {
		
		List<Double> res = Arrays.asList(new Double[n]);
		res.set(0, _S);
		double[] change = _generator.getVector(n-1);
		boolean zero = false;
		double curr_S = _S;
		
		for (int i=1; i<n; i++) {
			if (zero) {
				res.set(i, (double) 0);
			} else {
				curr_S = Math.max(curr_S + change[i-1], 0);
				if (curr_S == 0) zero = true;
				res.set(i, curr_S);
			}
		}
		return res;
	}
}
