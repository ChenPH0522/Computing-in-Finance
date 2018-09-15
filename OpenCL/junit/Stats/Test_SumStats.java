package Stats;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_SumStats {

	@Test
	public void test_SumStats() {
		
		float[] arr = new float[]{1,2,3,4,5};
		assertEquals(3, SumStats.getMean(arr), 0.00001);
	}
}
