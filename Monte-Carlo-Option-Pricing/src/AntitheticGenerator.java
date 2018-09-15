package MonteCarlo;

/**
 * 
 * @author Penghao Chen
 * This decorator takes a RandomVectorGenerator as input, and gives a negated
 * random vecotor. This sole purpose of this class is to speed up simulation.
 * 
 * Warning: if the underlying RandomVectorGenerator does not depend on a symmetric
 * distribution, this decorator will distort the result. 
 */

public class AntitheticGenerator implements RandomVectorGenerator {

	private RandomVectorGenerator _generator;
	private boolean _antithetic = false;
	private double[] _tempVector;
	
	//Constructor
	public AntitheticGenerator(RandomVectorGenerator RVG) {this._generator = RVG;}
	
	/**
	 * @param n		length of the array to be generated
	 * @return		an array of length n, with each element generated from the 
	 * distrbution underlyies the underlying RandomVectorGenerator.
	 */
	@Override
	public double[] getVector(int n) {
		
		if (this._antithetic) {
			this._antithetic = !this._antithetic;
			return this.negate(this._tempVector);
		}
		
		this._tempVector = this._generator.getVector(n);
		this._antithetic = !this._antithetic;
		return this._tempVector;
	}
	
	/**
	 * 
	 * @param arr	the array whose each element to be negated 
	 * @return		the negated array
	 */
	private double[] negate(double[] arr) {
		int n = arr.length;
		double[] new_res = new double[n];
		for (int i=0; i<n; i++) {new_res[i] = arr[i] * -1;}
		return new_res;
	}

}
