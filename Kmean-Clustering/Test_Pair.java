package ClusterHW;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_Pair {

	@Test
	public void testCompareTo() {
		
		// two nulls compare
		Pair p1 = new Pair();
		Pair p2 = new Pair();
		try {
			p1.compareTo(p2);
		} catch (IllegalArgumentException e) {
			String msg = "Pairs not comparable";
			assertEquals(msg, e.getMessage());
		}
		
		// one null compare
		p1._o1 = 1;
		try {
			p1.compareTo(p2);
		} catch (IllegalArgumentException e) {
			String msg = "Pairs not comparable";
			assertEquals(msg, e.getMessage());
		}
		
		// two inconsistent type 1st entries, compare
		p2._o1 = "Hello";
		try {
			p1.compareTo(p2);
		} catch (IllegalArgumentException e) {
			String msg = "Pairs not comparable";
			assertEquals(msg, e.getMessage());
		}
		
		// two consistent type 1st entries, compare
		p2._o1 = 0;
		assertTrue(p1.compareTo(p2)>0);
		p2._o1 = 2;
		assertTrue(p1.compareTo(p2)<0);
		p2._o1 = 1;
		assertTrue(p1.compareTo(p2)==0);
	}
	
	@Test
	public void testEquals() {
		
		// null 1st entry
		Pair p1 = new Pair();
		Pair p2 = new Pair();
		try {
			p1.equals(p2);
		} catch (IllegalArgumentException e) {
			String msg = "null 1st entry";
			assertEquals(msg, e.getMessage());
		}
		
		// null 2nd entry
		p1._o1 = 1;
		p2._o1 = 0;
		try {
			p1.equals(p2);
		} catch (IllegalArgumentException e) {
			String msg = "null 2nd entry";
			assertEquals(msg, e.getMessage());
		}
		
		// double-point pairs
		p1._o2 = new Point(new double[] {1,1});
		p2._o2 = new Point(new double[] {1,1});
		assertFalse(p1.equals(p2));
		p1._o1 = 0;
		assertTrue(p1.equals(p2));
	}
}
