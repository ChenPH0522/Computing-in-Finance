package ClusterHW;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_DoubleEquals {

	@Test
	public void test() {
		
		assertTrue(DoubleEquals.equals(1, 1.001, 0.01));
		assertFalse(DoubleEquals.equals(1, 1.1, 0.01));
		assertTrue(DoubleEquals.equals(1, 1, 0));
		try {
			DoubleEquals.equals(1, 1, -1);
		} catch (IllegalArgumentException e) {
			String msg = "precision cannot be negative";
			assertEquals(msg, e.getMessage());
		}
	}
}
