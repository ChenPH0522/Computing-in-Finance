import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

public class RecordTest {

	@Test
	public void test() {
		//test empty constructor & getters
		Record a = new Record();
		Date date1 = toDate("1/1/1900");
		Assert.assertTrue(a.getDate().equals(date1));
		Assert.assertEquals(0, a.getOpen(), 0);
		Assert.assertEquals(0, a.getHigh(), 0);
		Assert.assertEquals(0, a.getLow(), 0);
		Assert.assertEquals(0, a.getClose(), 0);
		Assert.assertEquals(0, a.getVol());
		Assert.assertEquals(0, a.getAdjClose(), 0);
		
		//test constructor with wrong inputs
		try {
			Record b = new Record("9/11/2017", 91.3, 90, 89, 87, 123987112, 88);
		} catch (IllegalArgumentException e) {
			String msg = "High or low price not correct";
			assertEquals(msg, e.getMessage());
		}
		
		//test constructor with good inputs & setters
		Record b = new Record("9/11/2017", 91.3, 99, 89, 90, 1000000000, 90.3);
		Date date2 = toDate("9/11/2017");
		Assert.assertTrue(b.getDate().equals(date2));
		Assert.assertEquals(91.3, b.getOpen(), 0);
		Assert.assertEquals(99, b.getHigh(), 0);
		Assert.assertEquals(89, b.getLow(), 0);
		Assert.assertEquals(90, b.getClose(), 0);
		Assert.assertEquals(1000000000, b.getVol());
		Assert.assertEquals(90.3, b.getAdjClose(), 0);
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
