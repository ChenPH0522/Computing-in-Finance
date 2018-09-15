import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {
	private Date date;
	private double open;
	private double high;
	private double low;
	private double close;
	private long vol;
	private double adjClose;
	
	//constructor
	public Record() {
		this("1/1/1900", 0.0, 0.0, 0.0, 0.0, (long)0, 0.0);
	}
	
	//constructor
	/**
	 * @param dateStr 		must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param open			double
	 * @param high			double
	 * @param low			double
	 * @param close			double
	 * @param vol			long
	 * @param adjClose		double
	 * @exception throw an exception if high is not the highest price 
	 * or low is not the lowest price
	 */
	public Record(String dateStr, double open, double high,
			double low, double close, long vol, double adjClose) {
		
		if ((high < open) 
				|| (high < low) 
				|| (high < close) 
				|| (low > open)
				|| (low > close)) {
			throw new IllegalArgumentException("High or low price not correct");
		}
		
		this.setDate(dateStr);
		this.setOpen(open);
		this.setHigh(high);
		this.setLow(low);
		this.setClose(close);
		this.setVol(vol);
		this.setAdjClose(adjClose);
	}
	
	public Date getDate() {
		return date;
	}
	
	public double getOpen() {
		return open;
	}
	
	public double getHigh() {
		return high;
	}
	
	public double getLow() {
		return low;
	}
	
	public double getClose() {
		return close;
	}
	
	public long getVol() {
		return vol;
	}
	
	public double getAdjClose() {
		return adjClose;
	}

	/**
	 * @param str	must be of the form "mm/dd/yyyy", with or without leading zeros
	 */
	public void setDate(String dateStr) {

		DateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
			System.out.println(e.getMessage());;
		}
	}

	public void setOpen(double open) {
		this.open = open;
	}
	
	public void setHigh(double high) {
		this.high = high;
	}
	
	public void setLow(double low) {
		this.low = low;
	}
	
	public void setClose(double close) {
		this.close = close;
	}
	
	public void setVol(long vol) {
		this.vol = vol;
	}
	
	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
}
