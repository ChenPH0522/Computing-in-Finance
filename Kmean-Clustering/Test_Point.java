package ClusterHW;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class Test_Point {
	
	@Test
	public void testConstructor() {
		
		// no argument
		Point a = new Point();
		assertEquals(0, a._coordinates.length);
		assertFalse(a.isTaken());
		
		// with arguments
		Point b = new Point(new double[] {1,2});
		Assert.assertArrayEquals(new double[] {1,2}, b._coordinates, 0);
		assertFalse(b.isTaken());
	}
	
	@Test
	public void testEquals() {

		Point a = new Point();
		Point b = new Point();
		assertTrue(a.equals(b));
		
		a._coordinates = new double[] {1,1};
		b._coordinates = new double[] {1,2};
		assertFalse(a.equals(b));
		
		a._coordinates = new double[] {1,1};
		b._coordinates = new double[] {1,1};
		assertTrue(a.equals(b));
		
		a._coordinates = new double[] {1,1.0000001};
		assertTrue(a.equals(b));
	}
	
	@Test
	public void testGetDistance() {
		
		Point a = new Point();
		Point b = new Point();
		Assert.assertEquals(0, a.getDistance(b), 0);
		
		Point c = new Point(new double[] {0,0});
		Point d = new Point(new double[] {1,1});
		Assert.assertEquals(1.414, c.getDistance(d), 0.001);
	}
	
	@Test
	public void testHashCode() {
		
		Point a = new Point();
		assertEquals(1, a.hashCode());
		
		Point b = new Point(new double[] {0.001});
		assertEquals(31, b.hashCode());
		
		Point c = new Point(new double[] {0});
		int hashB = b.hashCode();
		assertEquals(hashB, c.hashCode());
		
		Point d = new Point(new double[] {1,1});
		Point e = new Point(new double[] {1.5, 1.5});
		assertTrue(d.hashCode() != e.hashCode());
	}

	@Test
	public void testClosestPoint() {
		
		ArrayList<Point> points = new ArrayList<Point>();
		Point a = new Point();
		Point b = new Point();
		points.add(b);
		points.add(a);
		assertEquals(a, a.closestPoint(points));
		
		a._coordinates = new double[] {0,0};
		b._coordinates = new double[] {1,1};
		Point c = new Point(new double[] {0.5, 0.5});
		assertEquals(a, c.closestPoint(points));
		
		points.add(b);
		assertEquals(b, c.closestPoint(points));
		
		Point d = new Point(new double[] {0, Double.MAX_VALUE});
		points.remove(b);
		points.remove(a);
		points.remove(b);
		points.add(d);
		assertEquals(d, c.closestPoint(points));
	}
	
	@Test
	public void testClosestNUntaken() {
		
		// null point
		Point p1 = new Point();
		ArrayList<Point> points = new ArrayList<>();
		for (int i=0; i<4; i++) {
			points.add(new Point(new double[] {i,i}));
		} // points: [0,0], [1,1], [2,2], [3,3], [4,4]
		try {
			p1.closestNUntaken(points, 1);
		} catch (IllegalArgumentException e) {
			String msg = "points of different dimensions";
			assertEquals(msg, e.getMessage());
		}
		
		// null point list
		try {
			p1.closestNUntaken(null, 1);
		} catch (IllegalArgumentException e) {
			String msg = "null or empty input";
			assertEquals(msg, e.getMessage());
		}
		
		// empty list
		ArrayList<Point> emptyPoints = new ArrayList<>();
		try {
			p1.closestNUntaken(emptyPoints, 1);
		} catch (IllegalArgumentException e) {
			String msg = "null or empty input";
			assertEquals(msg, e.getMessage());
		}
		
		// good list, without taken
		p1._coordinates = new double[] {-1,-1};
		Point closest = p1.closestNUntaken(points, 1).get(0);
		Point origin = new Point(new double[] {0,0});
		assertEquals(origin, closest);
		
		// good list, with taken
		points.get(0).setTaken(true);
		// points: [0,0](taken), [1,1], [2,2], [3,3], [4,4]
		closest = p1.closestNUntaken(points, 1).get(0);
		Point p2 = new Point(new double[] {1,1});
		assertEquals(p2, closest);
		
		// good list, want more
		ArrayList<Point> ptslist = p1.closestNUntaken(points, 3);
		// System.out.println(ptslist);
		// Test passed
	}
	
	@Test
	public void testMaxDistance() {
		
		// null input
		try {
			Point.maxDistance(null, null);
		} catch (IllegalArgumentException e) {
			String msg = "null input";
			assertEquals(msg, e.getMessage());
		}
		
		// empty list
		ArrayList<Point> pts1 = new ArrayList<>();
		ArrayList<Point> pts2 = new ArrayList<>();
		try {
			Point.maxDistance(pts1, pts2);
		} catch (IllegalArgumentException e) {
			String msg = "empty list";
			assertEquals(msg, e.getMessage());
		}
		
		// list of different sizes
		for (int i=0; i<4; i++) {pts1.add(new Point(new double[] {i,i}));}
		//pts1: [0,0], [1,1], [2,2], [3,3]
		for (int i=0; i<5; i++) {pts2.add(new Point(new double[] {i,i}));}
		//pts2: [0,0], [1,1], [2,2], [3,3], [4,4]
		try {
			Point.maxDistance(pts1, pts2);
		} catch (IllegalArgumentException e) {
			String msg = "point lists of different sizes";
			assertEquals(msg, e.getMessage());
		}
		
		// good lists
		pts1.add(new Point(new double[] {0,0}));
		Assert.assertEquals(5.65685, Point.maxDistance(pts1, pts2), Point.getPrecision());
	}
	
	@Test
	public void testGetDistances() {
		
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<5; i++) {
			pts.add(new Point(new double[] {i,i}));
		}
		Point pt = new Point(new double[] {0,0});
		double[] dist = pt.getDistance(pts);
		/*
		for (int i=0; i<5; i++) {
			System.out.println(dist[i]);
		} */
	}
	
	@Test
	public void testToString() {
		
		Point p = new Point(new double[] {1,1});
		String coord = "[1.0 1.0 ]";
		assertEquals(coord, p.toString());
	}
}
