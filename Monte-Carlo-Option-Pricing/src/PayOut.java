package MonteCarlo;

public interface PayOut {
	
	public double getStrike();
	public void setStrike(double K);
	public double getPayout(StockPath path);

}
