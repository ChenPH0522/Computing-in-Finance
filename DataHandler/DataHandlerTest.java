import static org.junit.Assert.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

public class DataHandlerTest {

	/*
	 * When testing private methods/field, change private methods/field to public/protected
	 * Do the testing, then change it back to private.
	 * 
	 * Private fields & methods include:
	 * 	data 	(field)
	 * 	quickSort	(method)
	 * 	choosePivot	(method)
	 * 	partition	(method)
	 * 	bubbleSort	(method)
	 * 	swap	(method)
	 * 	isDateSorted	(method)
	 * 	toDate	(method)
	 *
	 * The test is based on the original prices.csv file (Date ascendingly sorted).
	 */
	

	@Test
	// TODO: data(field) is changed to public
	public void testConstructor() {
		DataHandler dh = new DataHandler();
		assertTrue(dh.data.isEmpty());
	}
	
	
	@Test
	public void testLoad() throws IOException {
		
		// input a bad path name
		DataHandler dh1 = new DataHandler();
		String path1 = "iuashfd";
		try {
			dh1.load(path1);
		} catch (IOException e) {
			String msg = path1 + " (The system cannot find the file specified)";
			assertEquals(msg, e.getMessage());
		}
		
		// input a good path name, but with bad data
		DataHandler dh2 = new DataHandler();
		String path2 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\pricesBad.csv";
		try {
			ArrayList<Record> price2 = dh2.load(path2);
		} catch (Exception e) {
			String msg = "unparseable date"; //: " + "\"abcdefg\"";
			assertEquals(msg, e.getMessage());
		}
		
		// input a good path name, with good data
		DataHandler dh3 = new DataHandler();
		String path3 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		ArrayList<Record> price3 = dh3.load(path3);
		int size = price3.size();
		Date date = toDate("7/1/2011");
		assertTrue(date.equals(price3.get(size-1).getDate()));
		Assert.assertEquals(132.09, price3.get(size-1).getOpen(), 0);
		Assert.assertEquals(134.1, price3.get(size-1).getHigh(), 0);
		Assert.assertEquals(131.78, price3.get(size-1).getLow(), 0);
		Assert.assertEquals(133.92, price3.get(size-1).getClose(), 0);
		Assert.assertEquals(202370700, price3.get(size-1).getVol(), 0);
		Assert.assertEquals(133.92, price3.get(size-1).getAdjClose(), 0);
	}

	
	@Test
	//TODO: swap (method) is made public; data (field) is made public
	public void testSwap() throws IOException {
		DataHandler dh = new DataHandler();
		String path = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh.data = dh.load(path);
		int size = dh.data.size();
		dh.swap(0, size-1);
		Assert.assertEquals(43.97, dh.data.get(size-1).getOpen(), 0);
		Assert.assertEquals(133.92, dh.data.get(0).getAdjClose(), 0);
	}
	
	
	@Test
	//TODO: bubble(method) is made public; data (field) is made public
	public void testBubbleSort() throws IOException {
		DataHandler dh = new DataHandler();
		String path = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh.data = dh.load(path);
		int size = dh.data.size();
		
		// price + ascending
		dh.bubbleSort(0, size-1, "prc", 1);
		Assert.assertEquals(31.27, dh.data.get(0).getAdjClose(), 0);
		
		// price + descending
		dh.bubbleSort(0, size-1, "prc", -1);
		Assert.assertEquals(144.94, dh.data.get(0).getAdjClose(), 0);
		
		// date + ascending
		dh.bubbleSort(0, size-1, "dt", 1);
		Assert.assertEquals(43.75, dh.data.get(0).getLow(), 0);
		
		// date + descending
		dh.bubbleSort(0, size-1, "dt", -1);
		Assert.assertEquals(131.78, dh.data.get(0).getLow(), 0);
	}
	
	
	@Test
	//TODO: choosePivot (method) is made public; data (field) is made public
	public void testChoosePivot() throws IOException {
		DataHandler dh = new DataHandler();
		String path = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh.data = dh.load(path);
		
		ArrayList<Object> pivots = dh.choosePivot(0, 9);
		Assert.assertEquals(32.37, (Double) pivots.get(0), 0);
		Date pivotDate = toDate("2/4/1993");
		assertTrue(pivotDate.equals(pivots.get(1)));
	}
	
	
	@Test
	//TODO: partition (method) is made public; bubbleSort (method) is made public; 
	//		data (field) is made public
	public void testPartition() throws IOException {
		
		// price ascending
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.data = dh1.load(path1);
		ArrayList<Object> pivots1 = dh1.choosePivot(0, 9);
		int pos1 = dh1.partition(0, 9, pivots1, "prc", 1);
		assertEquals(7, pos1);
		
		// price descending
		DataHandler dh2 = new DataHandler();
		String path2 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh2.data = dh2.load(path2);
		ArrayList<Object> pivots2 = dh2.choosePivot(0, 9);
		int pos2 = dh2.partition(0, 9, pivots2, "prc", -1);
		assertEquals(4, pos2);
		
		// date ascending
		DataHandler dh3 = new DataHandler();
		String path3 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh3.data = dh3.load(path3);
		dh3.bubbleSort(0, 9, "dt", -1);
		ArrayList<Object> pivot3 = dh3.choosePivot(0, 9);
		int pos3 = dh3.partition(0, 9, pivot3, "dt", 1);
		assertEquals(5, pos3);
		
		// date descending
		DataHandler dh4 = new DataHandler();
		String path4 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh4.data = dh4.load(path4);
		dh4.bubbleSort(0, 9, "prc", 1);
		ArrayList<Object> pivot4 = dh4.choosePivot(0, 9);
		int pos4 = dh4.partition(0, 9, pivot4, "dt", -1);
		assertEquals(6, pos4);
	}
	
	
	@Test
	//TODO: quickSort (method) is made public; data (field) is made public
	public void testQuickSort() throws IOException {
		// empty data
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\pricesEmpty.csv";
		dh1.data = dh1.load(path1);
		int size1 = dh1.data.size();
		dh1.quickSort(0, size1-1, "prc", 1);
		ArrayList<Record> emptyList = new ArrayList<Record>();
		assertEquals(emptyList, dh1.data);
		
		// 2-entry data
		DataHandler dh2 = new DataHandler();
		String path2 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh2.data = dh2.load(path2);
		int size2 = dh2.data.size();
		dh2.quickSort(0, 1, "prc", -1);
		Assert.assertEquals(31.88, dh2.data.get(0).getAdjClose(),0);
		
		// full data
		// price ascending
		dh2.quickSort(0, size2-1, "prc", 1);
		Assert.assertEquals(31.27, dh2.data.get(0).getAdjClose(), 0);
		// price descending
		dh2.quickSort(0, size2-2, "prc", -1);
		Assert.assertEquals(144.81, dh2.data.get(0).getAdjClose(), 0);
		// date ascending
		dh2.quickSort(0, size2-1, "dt", 1);
		Assert.assertEquals(31.65, dh2.data.get(0).getAdjClose(), 0);
		// date descending
		dh2.quickSort(1, size2-1, "dt", -1);
		Assert.assertEquals(133.92, dh2.data.get(1).getAdjClose(), 0);
	}
	
	
	@Test
	//TODO: quickSort (method) is made public; data (field) is made public
	public void testSort() throws IOException {
		
		// no argument
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.data = dh1.load(path1);
		int size1 = dh1.data.size();
		dh1.quickSort(0, size1-1, "prc", 1); // randomnize dates
		dh1.sort();
		Assert.assertEquals(31.65, dh1.data.get(0).getAdjClose(), 0);
		
		// invalid sorting method argument
		try {
			dh1.sort("asdif", "dt", 1);
		} catch (IllegalArgumentException e) {
			String msg1 = "invalid sorting method";
			assertEquals(msg1, e.getMessage());
		}
		
		// invalid sorting keyword argument
		try {
			dh1.sort("qk", "adfa", 1);
		} catch (IllegalArgumentException e) {
			String msg2 = "invalid sorting keyword";
			assertEquals(msg2, e.getMessage());
		}
		
		// invalid sorting order arugment
		try {
			dh1.sort("qk", "prc", 300);
		} catch (IllegalArgumentException e) {
			String msg3 = "invalid sorting order";
			assertEquals(msg3, e.getMessage());
		}
		
		// quicksort
		dh1.sort("qk", "prc", 1);
		Assert.assertEquals(31.27, dh1.data.get(0).getAdjClose(), 0);
		
		// bubblesort
		dh1.sort("bub", "dt", 1);
		Assert.assertEquals(133.92, dh1.data.get(size1-1).getAdjClose(), 0);
	}
	
	
	@Test
	//TODO: data (field) is made public
	public void testLoadPriceData() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		Assert.assertEquals(31.27, dh1.data.get(0).getAdjClose(), 0);
	}
	
	
	@Test
	//TODO: isDateSorted (method) is made public
	public void testIsDateSorted() throws IOException {
		
		// unsorted
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		assertEquals(0, dh1.isDateSorted());
		
		// ascendingly sorted
		dh1.sort();
		assertEquals(1, dh1.isDateSorted());
		
		// descendingly sorted
		dh1.sort("qk", "dt", -1);
		assertEquals(-1, dh1.isDateSorted());
	}
	
	
	// toDate (method) is already tested in this test class
	
	
	@Test
	public void testGetPrice() throws IOException {
		
		// data not ascendingly sorted by date
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		try {
			dh1.getPrices();
		} catch (IllegalArgumentException e) {
			String msg = "data not sorted ascendingly by date";
			assertEquals(msg, e.getMessage());
		}
		
		// data sorted by date ascendingly
		dh1.sort();
		ArrayList<Double> price1 = dh1.getPrices();
		Assert.assertEquals(31.65, price1.get(0), 0);
		
		// overloaded getPrice, reversed dates
		try {
			dh1.getPrices("9/13/2017", "9/1/2017");
		} catch (IllegalArgumentException e) {
			String msg = "reversed dates";
			assertEquals(msg, e.getMessage());
		}
		
		// dates out of bounce / empty return
		ArrayList<Double> price2 = dh1.getPrices("9/1/2017", "9/13/2017");
		assertTrue(price2.isEmpty());
		
		// get part of the data
		ArrayList<Double> price3 = dh1.getPrices("1/30/1993", "2/13/1993");
		Assert.assertEquals(31.88, price3.get(0), 0);
	}
	
	
	@Test
	public void testComputeMax() throws IOException {
		
		// data not ascendingly sorted by date
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		try {
			dh1.computeMax();
		} catch (IllegalArgumentException e) {
			String msg = "data not sorted ascendingly by date";
			assertEquals(msg, e.getMessage());
		}
		
		// data sorted by date ascendingly
		dh1.sort();
		double max1 = dh1.computeMax();
		Assert.assertEquals(144.94, max1, 0);
		
		// overloaded computeMax, reversed dates
		try {
			double max2 = dh1.computeMax("10/1/2015", "10/1/2014");
		} catch (IllegalArgumentException e) {
			String msg = "reversed dates";
			assertEquals(msg, e.getMessage());
		}
		
		// dates out of bounce
		double max3 = dh1.computeMax("10/1/2014", "10/1/2015");
		Assert.assertEquals(-1, max3, 0);
		
		// get part of the data
		double max4 = dh1.computeMax("1/1/1993", "2/1/1993");
		Assert.assertEquals(31.88, max4, 0);
	}
	
	
	@Test
	public void testComputeAverage() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		
		// data not ascendingly sorted by date
		try {
			dh1.computeAverage();
		} catch (IllegalArgumentException e) {
			String msg = "data not sorted ascendingly by date";
			assertEquals(msg, e.getMessage());
		}
		
		// data sorted by date ascendingly
		dh1.sort();
		double avg1 = dh1.computeAverage();
		Assert.assertEquals(89.792, avg1, 0.001);
		
		// overloaded method, reversed dates
		try {
			double avg2 = dh1.computeAverage("9/13/2017", "9/1/2017");
		} catch (IllegalArgumentException e) {
			String msg = "reversed dates";
			assertEquals(msg, e.getMessage());
		}
		
		// dates out of bounce
		double avg3 = dh1.computeAverage("7/1/2011", "9/1/2011");
		Assert.assertEquals(133.92, avg3, 0);
		
		// date within range
		double avg4 = dh1.computeAverage("1/30/1993", "2/13/1993");
		Assert.assertEquals(32.22, avg4, 0.001);
	}
	
	
	@Test
	public void testComputeMovingAverage() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		
		// data not ascendingly sorted by date
		try {
			ArrayList<Double> price1 = dh1.computeMovingAverage(10, "2/1/1993", "3/1/1993");
		} catch (IllegalArgumentException e) {
			String msg = "data not sorted ascendingly by date";
			assertEquals(msg, e.getMessage());
		}
		
		// invalid window size
		dh1.sort();
		try {
			ArrayList<Double> price2 = dh1.computeMovingAverage(0, "2/1/1993", "3/1/1993");
		} catch (IllegalArgumentException e) {
			String msg = "invalid window size";
			assertEquals(msg, e.getMessage());
		}
		
		// reversed dates
		try {
			ArrayList<Double> price3 = dh1.computeMovingAverage(10, "1/1/1994", "1/1/1993");
		} catch (IllegalArgumentException e) {
			String msg = "reversed dates";
			assertEquals(msg, e.getMessage());
		}
		
		// date difference < window size
		ArrayList<Double> price4 = dh1.computeMovingAverage(10, "1/30/1993", "2/1/1993");
		assertTrue(price4.isEmpty());
		
		// no data between dates
		ArrayList<Double> price5 = dh1.computeMovingAverage(1, "2/13/1993", "2/15/1993");
		assertTrue(price5.isEmpty());
		
		// date out of bounce / partial data
		ArrayList<Double> price6 = dh1.computeMovingAverage(2, "6/30/2011", "7/30/2011");
		Assert.assertEquals(132.945, price6.get(0), 0);
		
		// good window size, good dates
		ArrayList<Double> price7 = dh1.computeMovingAverage(10, "1/29/1993", "2/11/1993");
		Assert.assertEquals(32.173, price7.get(0), 0);
	}
	
	
	@Test
	//TODO: data (field) is made public
	public void testInsertRemovePrice() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "prc", 1);
		Record rec1 = new Record();
		
		// not sorted ascendingly by date
		try {
			dh1.insertPrice(rec1);
		} catch (IllegalArgumentException e) {
			String msg = "data not sorted by date";
			assertEquals(msg, e.getMessage());
		}
		
		// remove non-existent record
		dh1.removePrice(rec1);
		Assert.assertEquals(31.27, dh1.data.get(0).getAdjClose(), 0);
		
		// insert in ascending order, new record
		dh1.sort();
		dh1.insertPrice(rec1);
		Assert.assertEquals(0, dh1.data.get(0).getAdjClose(), 0);
		
		// remove  existing record
		dh1.removePrice(rec1);
		Assert.assertEquals(31.65, dh1.data.get(0).getAdjClose(), 0);
		
		// insert in ascending order, duplicate record
		Record rec2 = new Record("2/1/1993", 0, 0, 0, 0, 0, 0);
		dh1.insertPrice(rec2);
		Assert.assertEquals(0, dh1.data.get(1).getAdjClose(), 0);
		
		// insert in descending order, new record
		dh1.sort("qk", "dt", -1);
		Record rec3 = new Record();
		dh1.insertPrice(rec3);
		int size = dh1.data.size();
		Assert.assertEquals(0, dh1.data.get(size-1).getAdjClose(), 0);
		dh1.removePrice(rec3);
		
		// insert in descending order, duplicate record
		Record rec4 = new Record("6/26/2011", 0, 0, 0, 0, 0, 0);
		dh1.insertPrice(rec4);
		Assert.assertEquals(0, dh1.data.get(5).getAdjClose(), 0);
	}
	
	
	@Test
	//TODO: data (field) is made public
	public void testCorrectPrice() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "dt", 1);
		String path2 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\correctionsTest.csv";
		
		// insert new record 
		dh1.correctPrices(path2);
		Assert.assertEquals(100, dh1.data.get(0).getAdjClose(), 0);
		
		// overwrite existing record
		int size = dh1.data.size();
		Assert.assertEquals(101, dh1.data.get(size-1).getAdjClose(), 0);
	}
	
	@Test
	//TODO: data (field) is made public
	public void testClear() throws IOException {
		
		DataHandler dh1 = new DataHandler();
		String path1 = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing\\prices.csv";
		dh1.loadPriceData(path1, "qk", "dt", 1);
		dh1.clear();
		assertTrue(dh1.data.isEmpty());
	}
	
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
}
