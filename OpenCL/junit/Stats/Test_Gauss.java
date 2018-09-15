package Stats;

import static org.junit.Assert.*;

import org.junit.*;

public class Test_Gauss {
	
	@Test
	public void test_Gauss() {
		
		// pdf: z = 0
		double f_z = 1/Math.sqrt(2*Math.PI);
		assertEquals(f_z, Gauss.pdf(0, 0, 1), 0.0001);
		
		// cdf: z = 0
		assertEquals(0.5, Gauss.cdf(0, 0, 1), 0.0001);
		
		// inv_cdf: N(0.5) = 0
		assertEquals(0, Gauss.inv_cdf(0.5, 0, 1), 0.0001);
	}
}
