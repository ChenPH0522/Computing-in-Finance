package MonteCarlo;

import java.util.List;

public interface StockPath {
	
	public int getPeriod();
	public List<Double> getPrices();
}
