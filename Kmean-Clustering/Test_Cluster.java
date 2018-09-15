package ClusterHW;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class Test_Cluster {

	@Test
	public void testConstructor() {
		Cluster cluster1 = new Cluster();
		assertNull(cluster1.getCentroid());
		assertTrue(cluster1.getNonCentroid().isEmpty());
		assertFalse(cluster1.isUpdated());
		// cluster1: centroid = null, nonCentroid = null, updated = false
		
		
		Point centroid1 = new Point(new double[] {1,1});
		HashSet<Point> nonCentroid1 = new HashSet<>();
		cluster1.setCentroid(centroid1);
		cluster1.setNonCentroid(nonCentroid1);
		// cluster1: centroid = centroid1, nonCentroid = nonCentroid1, updated = false
		Point point2 = new Point(new double[] {1,1});
		assertTrue(point2.equals(cluster1.getCentroid()));
		assertNotNull(cluster1.getNonCentroid());
		assertTrue(cluster1.getNonCentroid().isEmpty());
	}
	
	@Test
	public void testAdd() {
		
		Cluster cluster1 = new Cluster();
		Point c1 = new Point(new double[] {0,0});
		Point p1 = new Point(new double[] {1,0});
		Point p2 = new Point(new double[] {-1,0});
		Point p3 = new Point(new double[] {0,0});
		HashSet<Point> nonCentroid = new HashSet<Point>();
		cluster1.setNonCentroid(nonCentroid);
		cluster1.setCentroid(c1);		//cluster1: c1, HS{}, false
		cluster1.add(c1);	//cluster1: c1, HS{c1}, false
		cluster1.add(p1);	//cluster1: c1, HS{c1, p1}, false
		cluster1.add(p2);	//cluster1: c1, HS{c1, p1, p2}, false
		assertFalse(cluster1.add(p3));
		assertFalse(cluster1.isUpdated());
	}
	
	@Test
	public void testRemove() {
		
		Point c1 = new Point(new double[] {0,0});
		Point p1 = new Point(new double[] {1,0});
		Point p2 = new Point(new double[] {-1,0});
		Point p3 = new Point(new double[] {0,0});
		HashSet<Point> nonCentroid = new HashSet<Point>();
		nonCentroid.add(c1);
		nonCentroid.add(p1);
		Cluster cluster1 = new Cluster(c1, nonCentroid); //cluster1: c1, HS{c1, p1}, false
		assertFalse(cluster1.remove(p2));
		assertTrue(cluster1.remove(p3));
		assertEquals(1, cluster1.size());
	}
	
	@Test
	public void testContains() {
		
		Cluster cluster1 = new Cluster();
		Point c1 = new Point(new double[] {0,0});
		Point p1 = new Point(new double[] {1,0});
		Point p2 = new Point(new double[] {-1,0});
		Point p3 = new Point(new double[] {0,0});
		HashSet<Point> nonCentroid = new HashSet<Point>();
		cluster1.setNonCentroid(nonCentroid);
		cluster1.setCentroid(c1);		//cluster1: c1, HS{}, false
		assertFalse(cluster1.hasPoint(c1));
		
		cluster1.add(c1);	//cluster1: c1, HS{c1}, false
		cluster1.add(p1);	//cluster1: c1, HS{c1, p1}, false
		cluster1.add(p2);	//cluster1: c1, HS{c1, p1, p2}, false
		assertTrue(cluster1.hasPoint(p3));
		
		Point p4 = new Point();
		assertFalse(cluster1.hasPoint(p4));
	}

	@Test
	public void testReleaseCentroid() {
		
		// nothing(null) to release
		Cluster cluster1 = new Cluster();
		assertNull(cluster1.releaseCentroid());
		
		// something to release
		cluster1.setCentroid(new Point());
		assertEquals(0, cluster1.getCentroid().getDimension());
	}
	
	@Test
	public void testReset() {
		
		Cluster cluster1 = new Cluster();
		HashSet<Point> nonCentroid = new HashSet<Point>();
		cluster1.setNonCentroid(nonCentroid);	//cluster1: null, {}, flase
		for (int i=0; i<5; i++) {cluster1.getNonCentroid().add(new Point());}
		// cluster1: null, {p0, p1, p2, p3, p4}, false
		
		cluster1.reset(); // cluster: null, {}, false
		assertNull(cluster1.getCentroid());
		assertTrue(cluster1.getNonCentroid().isEmpty());
		assertFalse(cluster1.isUpdated());
	}
	
	@Test
	public void testUpdate() {
		
		// empty point list
		Cluster cluster1 = new Cluster();	//cluster1: null, {}, false
		try {
			cluster1.update();
		} catch (IllegalArgumentException e) {
			String msg = "empty point list";
			assertEquals(msg, e.getMessage());
		}
		
		// points of 0-dimension
		for (int i=0; i<5; i++) {cluster1.getNonCentroid().add(new Point());}
		// cluster1: centroid: null; nonCentroid:{p0, p1, p2, p3, p4}, false
		cluster1.update();
		assertEquals(0, cluster1.getCentroid().getDimension());
		assertTrue(cluster1.isUpdated());
		
		// points of 2-dimension
		cluster1.reset();	//cluster1: null, {}, false
		//cluster1.getCentroid().
		for (int i=0; i<5; i++) {
			cluster1.getNonCentroid().add(new Point(new double[] {i, i}));
		}
		// cluster1: null,{[0,0], [1,1], [2,2], [3,3], [4,4]}, false
		cluster1.update(); //cluster1: [2,2], {[0,0], [1,1], [2,2], [3,3], [4,4]}, true
		Point ccopy = new Point(new double[] {2,2});
		assertEquals(ccopy, cluster1.getCentroid());
		assertTrue(cluster1.isUpdated());
	}
	
	@Test
	public void testGetVar() {
		
		Cluster c = new Cluster(new Point(new double[] {0}));
		for (int i=0; i<5; i++) {
			c.add(new Point(new double[] {i}));
		}
		assertEquals(2, c.getVar(), Point.getPrecision());
		
		c = new Cluster(new Point(new double[] {0,0}));
		for (int i=0; i<5; i++) {
			c.add(new Point(new double[] {i,i}));
		} // c-pts: [0,0], [1,1], [2,2], [3,3], [4,4]
		assertEquals(4, c.getVar(), Point.getPrecision());
	}
	
	@Test
	public void testToString() {
		
		Cluster cluster1 = new Cluster();	//cluster1: null, {}, false
		String msg1 = "null; ";
		assertEquals(msg1, cluster1.toString());
		
		for (int i=0; i<5; i++) {cluster1.add(new Point(new double[] {i}));}
		// cluster1: null, {p0, p1, p2, p3, p4}, false
		String msg2 = "null; [0.0 ], [1.0 ], [2.0 ], [3.0 ], [4.0 ], "; 
		assertEquals(msg2, cluster1.toString());
	}
	
	@Test
	public void testGetDist() {
		Cluster c = new Cluster(new Point(new double[] {0}));
		c = new Cluster(new Point(new double[] {0,0}));
		for (int i=0; i<5; i++) {
			c.add(new Point(new double[] {i,i}));
		} // c: [0,0];[0,0], [1,1], [2,2], [3,3], [4,4]
		assertEquals(2.8284, c.getDist(), Point.getPrecision());
	}
}
