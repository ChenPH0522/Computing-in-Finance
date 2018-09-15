package MonteCarlo;

import java.util.List;

/**
 * @author Penghao Chen
 * Asian Call option pays max(average price, 0).
 */
public class AsianCall implements PayOut{
	
	private double _strike = 0;
	
	//getters & setters
	public double getStrike() {return _strike;}
	public void setStrike(double strike) {_strike = strike;}
	
	//constructor
	public AsianCall() {}
	/**
	 * @param strike	strike price. Default is 0.
	 */
	public AsianCall(double strike) {_strike = strike;}
	
	/**
	 * @param path	a stock path generator
	 * @return the payoff of the option, based on the stock path generator 
	 */
	@Override
	public double getPayout(StockPath path) {
		
		List<Double> stock_Path = path.getPrices();
		double sum = 0;
		int l = stock_Path.size();
		for (int i=0; i<l; i++) {
			sum += stock_Path.get(i);
		}
		double average = sum/l;
		
		return Math.max(average-_strike, 0);
	}
	
	/**
	 * @param path	the path of a movement	
	 * @return	the payoff of the option, based on the input movement path
	 */
	public double getPayout(double[] path) {
		
		int l = path.length;
		double sum = 0;
		for (int i=0; i<l; i++) {
			sum += path[i];
		}
		double average = sum/l;
		
		return Math.max(average-_strike, 0); 
	}
}
