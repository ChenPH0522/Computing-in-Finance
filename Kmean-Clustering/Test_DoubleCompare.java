package ClusterHW;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_DoubleCompare {

	@Test
	public void test() {
		assertEquals(0, DoubleCompare.compare(0, 0, 0));
		assertEquals(1, DoubleCompare.compare(1, 0, 0.5));
		assertEquals(-1, DoubleCompare.compare(-1, 0, 0.01));
	}

}
