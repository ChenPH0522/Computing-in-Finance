package ClusterHW;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.Assert;

public class Test_ComputeCentroid {

	@Test
	public void test() {
		
		ArrayList<Point> points = new ArrayList<>();
		
		// empty list
		try {
			ComputeCentroid.compute(points);
		} catch (IllegalArgumentException e) {
			String msg = "empty point list";
			assertEquals(msg, e.getMessage());
		}
		
		// 0-dim points
		for (int i=0; i<5; i++) {
			points.add(new Point());
		}
		Point c1 = new Point();
		assertTrue(c1.equals(ComputeCentroid.compute(points)));
		
		// dimensional inconsistent points
		Point px = new Point(new double[] {1});
		points.add(px);
		try {
			ComputeCentroid.compute(points);
		} catch (IllegalArgumentException e) {
			String msg = "point dimensions are inconsistent";
			assertEquals(msg, e.getMessage());
		}
		
		// 1-dim points
		ArrayList<Point> points2 = new ArrayList<>();
		for (int i=0; i<5; i++) {points2.add(new Point(new double[] {i}));}
		assertEquals(2, ComputeCentroid.compute(points2)._coordinates[0], 0);
		
		// 2-dim points
		ArrayList<Point> points3 = new ArrayList<>();
		for (int i=0; i<5; i++) {points3.add(new Point(new double[] {i, 4-i}));}
		Point c3 = new Point(new double[] {2,2});
		assertEquals(c3, ComputeCentroid.compute(points3));
	}

}
