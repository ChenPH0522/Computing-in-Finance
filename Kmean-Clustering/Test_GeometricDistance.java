package ClusterHW;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class Test_GeometricDistance {

	@Test
	public void test() {
		
		Distance dist = new GeometricDistance();
		
		Point a = new Point();
		Point b = new Point();
		Assert.assertEquals(0, dist.computeDistance(a, b), 0);
		
		Point c = new Point(new double[] {0,0});
		Point d = new Point(new double[] {1,1});
		Assert.assertEquals(1.414, dist.computeDistance(c, d), 0.01);
		
	}

}
