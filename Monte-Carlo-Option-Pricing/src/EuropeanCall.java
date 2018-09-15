package MonteCarlo;

import java.util.List;

/**
 * @author Penghao Chen
 * European call option pays max(ending price - strike, 0)
 */
public class EuropeanCall implements PayOut{

	private double _strike = 0;
	
	//getters & setters
	public double getStrike() {return _strike;}
	public void setStrike(double strike) {_strike = strike;}
	
	//constructors
	public EuropeanCall() {}
	/**
	 * @param strike 		strike price. Default is 0.
	 */
	public EuropeanCall(double strike) {_strike = strike;}

	/**
	 * @param path	a stock path generator
	 * @return	the payoff of the option, based on the stock path generator
	 */
	@Override
	public double getPayout(StockPath path) {
		List<Double> stock_Path = path.getPrices();
		int l = stock_Path.size();
		return Math.max(stock_Path.get(l-1) - _strike, 0);
	}
		
	/**
	 * @param path	the path of a movement	
	 * @return	the payoff of the option, based on the input movement path
	 */
	public double getPayout(double[] path) {
		int l = path.length;
		return Math.max(path[l-1] - _strike, 0);
	}
}
