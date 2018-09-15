import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Program that handles prices.csv and corrections.csv data. 
 * @author Penghao Chen
 */
public class DataHandler {

	public ArrayList<Record> data;
	
	//constructor;
	public DataHandler() {
		data = new ArrayList<>();
	}
	
	
	/**
	 * clear data
	 */
	public void clear() {
		data.clear();
	}
	
	
	/**
	 * Load the file according to the specified file path.
	 * Sort the data by the specified sorting method, sorting keyword, and sorting order
	 * @param path		Pathname string.
	 * @param method	String. Choose between "qk" (quick) or "bub" (bubble)
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 * @throws IOException	if the file cannot be found according to the given path.
	 */
	public void loadPriceData(String path, String method, 
			String key, int order) throws IOException {

		data = load(path);
		sort(method, key, order);
	}
	
	
	/**
	 * Loads price data from a csv file.
	 * @param path	Pathname string.
	 * @return	An ArrayList<Record>	
	 * @throws IOException	if the file cannot be found according to the given path.
	 */
	public ArrayList<Record> load(String path) throws IOException {

		ArrayList<Record> data = new ArrayList<>();
		
		Scanner scanner = new Scanner(new File(path));
		Scanner dataScanner = null;
		int index = 0;
		
		scanner.nextLine();
		
		while (scanner.hasNextLine()) {
			
			dataScanner = new Scanner(scanner.nextLine());
			dataScanner.useDelimiter(",");
			Record rec = new Record();

			while (dataScanner.hasNext()) {
				String field = dataScanner.next();
				
				if (index == 0)
					rec.setDate(field);
				else if (index == 1)
					rec.setOpen(Double.parseDouble(field));
				else if (index == 2)
					rec.setHigh(Double.parseDouble(field));
				else if (index == 3)
					rec.setLow(Double.parseDouble(field));
				else if (index == 4)
					rec.setClose(Double.parseDouble(field));
				else if (index == 5)
					rec.setVol(Long.parseLong(field));
				else
					rec.setAdjClose(Double.parseDouble(field));
				
				index++;
			}
			
			index = 0;
			data.add(rec);
		}
		
		scanner.close();
		return data;
	}
	
	
	/**
	 * Sort the data by specified method and key.
	 * If no input argument, QuickSort by date in ascending order. 
	 * @param method	String. Choose between "qk" (quick) or "bub" (bubble)
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 */
	public void sort() {
		this.sort("qk", "dt", 1);
	}
	
	
	/**
	 * Sort the data by specified method and key.
	 * If no input argument, QuickSort by date in ascending order. 
	 * @param method	String. Choose between "qk" (quick) or "bub" (bubble)
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 */
	public void sort(String method, String key, int order) {
		
		if ( ! (method.equals("qk") || method.equals("bub")))
			throw new IllegalArgumentException("invalid sorting method");
		
		if ( ! (key.equals("prc") || key.equals("dt")))
			throw new IllegalArgumentException("invalid sorting keyword");
		
		if ( ! (order==1 || order==-1))
			throw new IllegalArgumentException("invalid sorting order");
		
		if (data.size()>1)
			if (method.equals("qk"))
				quickSort(0, data.size()-1, key, order);
			else
				bubbleSort(0, data.size()-1, key, order);
	}
	
	
	/**
	 * Return an ArrayList of prices (adjusted close) between the start and the end date.
	 * This method assumes ascendingly sorted data by date.
	 * If no input argument, return the full adjClose price list. 
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate 	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return An ArrayList of prices (adjusted close) between the start and the end date,
	 * inclusive of both ends if they exit.
	 */
	public ArrayList<Double> getPrices() {
		
		if (isDateSorted() <= 0 ) 
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		int size = data.size();
		DateFormat df = new SimpleDateFormat("M/d/yyyy");
		String fromDate = df.format(data.get(0).getDate());
		String toDate = df.format(data.get(size-1).getDate());
		
		return this.getPrices(fromDate, toDate);
	}
	
	
	/**
	 * Return an ArrayList of prices (adjusted close) between the start and the end date.
	 * This method assumes ascendingly sorted data by date.
	 * If no input argument, return the full adjClose price list.  
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate 	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return An ArrayList of prices (adjusted close) between the start and the end date,
	 * inclusive of both ends if they exit.
	 */
	public ArrayList<Double> getPrices(String fromDate, String toDate) {
		
		if (isDateSorted() <= 0 ) 
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		Date date1 = toDate(fromDate);
		Date date2 = toDate(toDate);
		
		if (date1.after(date2)) {
			throw new IllegalArgumentException("reversed dates");
		}
		
		ArrayList<Double> priceList = new ArrayList<>();
		int i = 0;	
		
		while (i < data.size() 
				&& date2.compareTo(data.get(i).getDate()) >= 0) {
			if (date1.compareTo(data.get(i).getDate()) <= 0)
					priceList.add(data.get(i).getAdjClose());
			i++;
		}
		
		return priceList;
	}
	
	
	/**
	 * return the average price (adjusted close) for the period specified by the two dates.
	 * This method assumes data already sorted by date in ascending order.
	 * Inclusive of both ends, if exist.
	 * If no input argument, return the average adjClose price across all records  
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return	return -1 if there is no price data for the specified period.
	 */
	public double computeAverage() {
		
		if (isDateSorted() <= 0)
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		int size = data.size();
		DateFormat df = new SimpleDateFormat("M/d/yyyy");
		String fromDate = df.format(data.get(0).getDate());
		String toDate = df.format(data.get(size-1).getDate());
		
		return this.computeAverage(fromDate, toDate);	
	}
	
	
	/**
	 * return the average price (adjusted close) for the period specified by the two dates.
	 * This method assumes data already sorted by date in ascending order.
	 * Inclusive of both ends, if exist.
	 * If no input argument, return the average adjClose price across all records  
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return	return -1 if there is no price data for the specified period.
	 */
	public double computeAverage(String fromDate, String toDate) {

		if (isDateSorted() <= 0)
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		Date date1 = toDate(fromDate);
		Date date2 = toDate(toDate);
		
		if (date1.after(date2)) {
			throw new IllegalArgumentException("reversed dates");
		}
		
		int n = 0, i = 0;
		double sum = 0;
		
		while (i < data.size() 
				&& date2.compareTo(data.get(i).getDate()) >= 0) {
			if (date1.compareTo(data.get(i).getDate()) <= 0) {
				sum += data.get(i).getAdjClose();
				n++;
			}
			i++;
		}
		
		if (n == 0)
			return -1;
		return sum/n;
	}
	
	
	/**
	 * return the maximum price (adjusted close) for the period specified by the two dates.
	 * This method assumes data already sorted ascendingly by date.
	 * Inclusive of both ends, if exist.
	 * If no input argument, return the max adjClose price across all records
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return	return -1 if there is no price data for the specified period.
	 */
	public double computeMax() {

		if (isDateSorted() <= 0)
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		int size = data.size();
		DateFormat df = new SimpleDateFormat("M/d/yyyy");
		String fromDate = df.format(data.get(0).getDate());
		String toDate = df.format(data.get(size-1).getDate());
		
		return this.computeMax(fromDate, toDate);
	}
	
	
	/**
	 * return the maximum price (adjusted close) for the period specified by the two dates.
	 * This method assumes data already sorted ascendingly by date.
	 * Inclusive of both ends, if exist.
	 * If no input argument, return the max adjClose price across all records
	 * @param fromDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate	String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return	return -1 if there is no price data for the specified period.
	 */
	public double computeMax(String fromDate, String toDate) {
		
		if (isDateSorted() <= 0)
			throw new IllegalArgumentException("data not sorted ascendingly by date");
		
		Date date1 = toDate(fromDate);
		Date date2 = toDate(toDate);
		
		if (date1.after(date2)) {
			throw new IllegalArgumentException("reversed dates");
		}
		
		int i = 0;
		double max = -1; 
		
		while (i < data.size() 
				&& date2.compareTo(data.get(i).getDate()) >= 0) {
			if (date1.compareTo(data.get(i).getDate()) <= 0) {
				max = Math.max(max, data.get(i).getAdjClose());
			}
			i++;
		}
		
		return max;
	}

	
	/**
	 * Return a moving average of the last n elements of price,
	 * where n = windowSize and price = adjusted close.
	 * This method assumes data already sorted ascendingly by date.
	 * @param windowSize	must be positive integer.
	 * @param fromDate		String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @param toDate		String. Must be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return	An ArrayList of average prices. 
	 * If there is no price data between the dates,
	 * or the number of prices is smaller than the windowSize,
	 * return an empty list.
	 */
	public ArrayList<Double> computeMovingAverage(int windowSize,
			String fromDate, String toDate) {

		if (isDateSorted() <= 0)
			throw new IllegalArgumentException("data not sorted ascendingly by date");

		if (windowSize<1)
			throw new IllegalArgumentException("invalid window size");
		
		Date date1 = toDate(fromDate);
		Date date2 = toDate(toDate);
		
		if (date1.after(date2)) {
			throw new IllegalArgumentException("reversed dates");
		}
		
		ArrayList<Double> movingAverage = new ArrayList<>();
		int i = 0;
		
		while (i < data.size() && date1.after(data.get(i).getDate())) {
			i++;
		}
		
		if (i+windowSize-1 < data.size()) {
			
			double movingSum = 0;
			
			for (int j = 0; j < windowSize; j++) {
				movingSum += data.get(i+j).getAdjClose();
			}
			
			double last = 0, next = 0;
			
			while (i+windowSize-1 < data.size()
					&& !data.get(i+windowSize-1).getDate().after(date2)) {
				movingSum = movingSum + next - last;
				movingAverage.add(movingSum/windowSize);
				last = data.get(i).getAdjClose();
				if (i+windowSize < data.size())
					next = data.get(i+windowSize).getAdjClose();
				i++;
			}
		}
		
		return movingAverage;
	}
	
	
	/**
	 * Inserts a price into the price data.
	 * If the date already exists, overwrite the record that is already there.
	 * This method assumes data already sorted by date, ascendingly or descendingly,
	 * and it does not change the sorting order after insertion.
	 * @param record	Price record.
	 */
	public void insertPrice(Record record) {

		int i = 0;
		
		if (isDateSorted() == 1) {
			
			while (i < data.size()
					&& data.get(i).getDate().before(record.getDate())) {
				i++;
			}
			
			if (i == data.size()
					|| data.get(i).getDate().after(record.getDate())) {
				data.add(i, record);
			
			} else {
				
				data.set(i, record);
			}
		}
		
		else if (isDateSorted() == -1) {
			
			while (i < data.size()
					&& data.get(i).getDate().after(record.getDate())) {
				i++;
			}
			
			if (i == data.size()
					|| data.get(i).getDate().before(record.getDate())) {
				data.add(i, record);
			} else {
				
				data.set(i, record);
			}
		}
		
		else {
			throw new IllegalArgumentException("data not sorted by date");
		}
	}
	
	
	/**
	 * Removes a price out of the price data if and only if the date already exists.
	 * Otherwise, does nothing to the data.
	 * @param record	Price record.
	 */
	public void removePrice(Record record) {

		for  (int i = 0; i < data.size(); i++) {
			if (data.get(i).getDate().equals(record.getDate())) {
				data.remove(i);
				break;
			}
		}
	}

	
	/**
	 * Correct the price data according to the specified correction file.
	 * If the date already exists, the method corrects the price data.
	 * If the date does not exist, the method adds the price data.
	 * This method assumes the original data is sorted by date, ascendingly or descendingly,
	 * but not the correction data.
	 * @param path	Pathname string.
	 * @throws IOException
	 */
	public void correctPrices(String path) throws IOException {

		ArrayList<Record> correction = load(path);
		
		for (Record record: correction) {
			insertPrice(record);
		}
	}
	
	
	/**
	 * Quick sorts the data list by the specified key and order.
	 * @param fromIndex		Starting index of the data list to be sorted. Inclusive.
	 * @param toIndex		Ending index of the data list to be sorted. Inclusive.
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 */
	public void quickSort(int fromIndex, int toIndex, String key, int order) {
		
		if (toIndex - fromIndex < 1) return;
		if (toIndex - fromIndex < 3) bubbleSort(fromIndex, toIndex, key, order);

		if (toIndex - fromIndex >= 3) {
			
			ArrayList<Object> pivots = choosePivot(fromIndex, toIndex);
			int pivotIndex = 0;
			
			pivotIndex = partition(fromIndex, toIndex, pivots, key, order);
			
			quickSort(fromIndex, pivotIndex-1, key, order);
			quickSort(pivotIndex, toIndex, key, order);
		}
	}
	
	
	/**
	 * Auxilliary method of quickSort. Choose the pivot price and date used in quickSort.
	 * @param fromIndex		Starting index of the data list to be sorted. Inclusive.
	 * @param toIndex		Ending index of the data list to be sorted. Inclusive.
	 * @return	An ArrayList whose 1st element is the pivotPrice
	 * and whose 2nd element is the pivotDate.
	 */
	public ArrayList<Object> choosePivot(int fromIndex, int toIndex) {
		
		ArrayList<Object> pivots = new ArrayList<Object>();
		Double pivotPrice = 0.0;
		double price1 = data.get(fromIndex).getAdjClose();
		double price2 = data.get((fromIndex + toIndex)/2).getAdjClose();
		double price3 = data.get(toIndex).getAdjClose();
		
		if (price1 <= Math.max(price2, price3) && price1 >= Math.min(price2, price3))
			pivotPrice = price1;
		if (price2 <= Math.max(price1, price3) && price2 >= Math.min(price1, price3))
			pivotPrice = price2;
		if (price3 <= Math.max(price1, price2) && price3 >= Math.min(price1, price2))
			pivotPrice = price3;
		
		Date pivotDate = null;
		Date date1 = data.get(fromIndex).getDate();
		Date date2 = data.get((fromIndex + toIndex)/2).getDate();
		Date date3 = data.get(toIndex).getDate();
		
		if (date1.before(date2) && date1.before(date3)) {
			
			if (date2.before(date3)) {
				pivotDate = date2;
			} else {
				pivotDate = date3;
			}
		} 
		
		else if (date1.after(date2) && date1.after(date3)) {
			
			if (date3.after(date2)) {
				pivotDate = date3;
			} else {
				pivotDate = date2;
			}
		}
		
		else {
			pivotDate = date1;
		}
		
		pivots.add(pivotPrice);
		pivots.add(pivotDate);
		
		return pivots;
	}

	
	/**
	 * Auxilliary method of quickSort.
	 * Move everthing smaller (or larger) than the pivot to the left (or right) of the pivot.
	 * @param fromIndex		Starting index of the data list to be sorted. Inclusive.
	 * @param toIndex		Ending index of the data list to be sorted. Inclusive.
	 * @param pivots		An ArrayList with the 1st element being the pivot price and the
	 * 2nd element being the pivot Date.
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 * @return The index of the pivot position after partitioning.
	 */
	public int partition(int fromIndex, int toIndex,
			ArrayList<Object> pivots, String key, int order) {

		int i = fromIndex, j = toIndex;
		String spec = key+" "+order;
		
		if (spec.equals("prc 1")) {
			// price ascending
			
			double pivot = (Double) pivots.get(0);
			
			while (i < j) {
				
				while (i <= toIndex && pivot > data.get(i).getAdjClose()) {
					i++;
				}
				
				while (j >= fromIndex && pivot < data.get(j).getAdjClose()) {
					j--;
				}
				
				if (i <= toIndex && j >= fromIndex && i < j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			
			if (i < toIndex && pivot > data.get(i).getAdjClose()) {
				i++;
			}
		}
		
		if (spec.equals("prc -1")) {
			// price descending
			
			double pivot = (Double) pivots.get(0);
			
			while (i < j) {
				
				while (i <= toIndex && pivot < data.get(i).getAdjClose()) {
					i++;
				}
				
				while (j >= fromIndex && pivot > data.get(j).getAdjClose()) {
					j--;
				}
				
				if (i <= toIndex && j >= fromIndex && i < j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			
			if (i < toIndex && pivot < data.get(i).getAdjClose()) {
				i++;
			}
		}
		
		if (spec.equals("dt 1")) {
			// date ascending
			
			Date pivot = (Date) pivots.get(1);
			
			while (i < j) {
				
				while (i <= toIndex && pivot.compareTo(data.get(i).getDate()) > 0) {
					i++;
				}
				
				while (j >= fromIndex && pivot.compareTo(data.get(j).getDate()) < 0) {
					j--;
				}
				
				if (i <= toIndex && j >= fromIndex && i < j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			
			if (i < toIndex && pivot.compareTo(data.get(i).getDate()) > 0) {
				i++;
			}
		}
		
		if (spec.equals("dt -1")) {
			// date descending
			
			Date pivot = (Date) pivots.get(1);
			
			while (i < j) {
				
				while (i <= toIndex && pivot.compareTo(data.get(i).getDate()) < 0) {
					i++;
				}
				
				while (j >= fromIndex && pivot.compareTo(data.get(j).getDate()) > 0) {
					j--;
				}
				
				if (i <= toIndex && j >= fromIndex && i < j) {
					swap(i, j);
					i++;
					j--;
				}
			}
			
			if (i < toIndex && pivot.compareTo(data.get(i).getDate()) < 0) {
				i++;
			}
			
		}
		
		return i;
	}
	
	
	/**
	 * Bubble sorts the data list by the specified key and order.
	 * @param fromIndex		Starting index of the data list to be sorted. Inclusive.
	 * @param toIndex		Ending index of the data list to be sorted. Inclusive.
	 * @param data		field of DataHandler
	 * @param key		String. Choose between "prc" (price) or "dt" (date)
	 * @param order		int. Choose between 1 (ascending) or -1 (descending)
	 */
	public void bubbleSort(int fromIndex, int toIndex, String key, int order) {

		int i = fromIndex, j = toIndex;
		
		switch (key+" "+order) {
		
		case "prc 1": {
			
			while (j > fromIndex) {
				while (i < j) {
					if (data.get(i).getAdjClose()>data.get(i+1).getAdjClose())
						swap(i, i+1);
					i++;
				}
				j--;
				i = fromIndex;
			}
		}
		
		case "prc -1": {
			
			while (j> fromIndex) {
				while (i < j) {
					if (data.get(i).getAdjClose()<data.get(i+1).getAdjClose())
						swap(i, i+1);
					i++;
				}
				j--;
				i = fromIndex;
			}
		}
		
		case "dt 1": {
			
			while (j > fromIndex) {
				while (i < j) {
					if (data.get(i).getDate().after(data.get(i+1).getDate()))
						swap(i, i+1);
					i++;
				}
				j--;
				i = fromIndex;
			}
		}
		
		case "dt -1": {
			
			while (j > fromIndex) {
				while (i < j) {
					if (data.get(i).getDate().before(data.get(i+1).getDate()))
						swap(i, i+1);
					i++;
				}
				j--;
				i = fromIndex;
			}
		}		
		
		}
		
	}
	
	
	/**
	 * Switch positions of two records in the data ArrayList.
	 * @param i		int. Index of the 1st Record.
	 * @param j		int. Index of the 2nd Record.
	 */
	public void swap(int i, int j) {

		Record temp = data.get(i);
		data.set(i, data.get(j));
		data.set(j, temp);
	}
	
	
	/**
	 * Check if the data is sorted by Date 
	 * @return return 1 if data is sorted in ascending order by date; 0 if not sorted; 
	 * -1 if sorted in descending order.
	 * If there are 0 or 1 element, or if all the elements are the same, return 1.
	 */
	public int isDateSorted() {

		if (data.size()<2) return 1;
		
		Date date1 = data.get(0).getDate(); 
		Date date2 = data.get(data.size()-1).getDate();
		int i = 1;
		
		if (date1.compareTo(date2) <= 0) {
			
			// ascending order
			while (i < data.size()) {
				date1 = data.get(i-1).getDate();
				date2 = data.get(i).getDate();
				if (date1.after(date2))
					return 0;
				i++;
			}
			return 1;
			
		} else {
			
			// descending order
			while (i < data.size()) {
				date1 = data.get(i-1).getDate();
				date2 = data.get(i).getDate();
				if (date1.before(date2))
					return 0;
				i++;
			}
			return -1;
		}
	}
	
	
	/**
	 * Converts an "mm/dd/yyyy" string to Date object
	 * @param dateStr String. Should be of the form "mm/dd/yyyy", with or without leading zeros
	 * @return a Date object
	 */
	private Date toDate(String dateStr) {
		
		DateFormat df = new SimpleDateFormat("M/d/yyyy");
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	/**
	 * Print required results to the results.txt file.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// task 1
		DataHandler priceData = new DataHandler();
		// task 2
		priceData.loadPriceData(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv",
				"qk", "dt", 1);
		// task 3
		priceData.correctPrices(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\corrections.csv");
		
		DecimalFormat formatter = new DecimalFormat("#0.00");
		
		try {
			
			// task 4
			ArrayList<Double> priceArray = priceData.getPrices("08/15/2004", "08/20/2004");
			
			PrintWriter writer = new PrintWriter(
					"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\results.txt", "UTF-8");
			writer.println("The prices of SPY between 08/15/2004 and 08/20/2004 are:");
			for (Double i: priceArray) {
				writer.println(i);
			}
			writer.println();
			
			// task 5
			double avgPrice = priceData.computeAverage("08/15/2004", "09/15/2004");
			writer.println("The Average Price of SPY between 08/15/2004 and 09/15/2004 is: "
					+ formatter.format(avgPrice));
			writer.println();
			
			// task 6
			double maxPrice = priceData.computeMax("04/15/2004", "06/15/2004");
			writer.println("The Maximum Price of SPY between 04/15/2004 and 06/15/2004 is: "
					+ maxPrice);
			writer.println();
			
			// task 7
			ArrayList<Double> movingAvg = priceData.computeMovingAverage(10,
					"08/15/2004", "09/15/2004");
			writer.println("The Moving Average of SPY between "
					+ "08/15/2004 and 09/15/2004 for WindowSize 10 is:");
			for (Double i: movingAvg) {
				writer.println(formatter.format(i));
			}
			
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}
}