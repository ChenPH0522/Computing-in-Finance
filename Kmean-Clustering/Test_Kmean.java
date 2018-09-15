package ClusterHW;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class Test_Kmean {

	@Test
	public void getCentroids() {
		
		//null clusters
		Kmean k1 = new Kmean();
		assertNull(k1.getCentroids());

		//empty clusters
		Cluster c1 = new Cluster();
		ArrayList<Cluster> clusters = new ArrayList<>();
		k1._clusters = clusters;
		assertNull(k1.getCentroids());
		
		//good clusters
		Cluster c2 = new Cluster();
		Cluster c3 = new Cluster();
		for (int i=0; i<1; i++) {c1.add(new Point(new double[] {i,i}));}
		for (int i=0; i<2; i++) {c2.add(new Point(new double[] {i,i}));}
		for (int i=0; i<3; i++) {c3.add(new Point(new double[] {i,i}));}
		c1.update();
		c2.update();
		c3.update();
		//c1: [0,0]; [0,0]
		//c2: [0.5, 0.5]; [0,0], [1,1]
		//c3: [1,1]; [0,0], [1,1], [2,2]
		k1._clusters.add(c1);
		k1._clusters.add(c2);
		k1._clusters.add(c3);
		ArrayList<Point> centers = new ArrayList<>();
		centers.add(new Point(new double[] {0,0}));
		centers.add(new Point(new double[] {0.5,0.5}));
		centers.add(new Point(new double[] {1,1}));
		assertEquals(centers.toString(), k1.getCentroids().toString());
	}
	
	@Test
	public void testChooseK() {
		
		// no enought points
		Kmean k1 = new Kmean();
		try {
			ArrayList<Point> Kpts = k1.chooseK(1);
		} catch (IllegalArgumentException e) {
			String msg = "points fewer than k";
			assertEquals(msg, e.getMessage());
		}
		
		// negative k
		ArrayList<Point> pts1 = new ArrayList<>(); 
		for (int i=0; i<5; i++) {
			pts1.add(new Point(new double[] {i,i}));
		}
		// k1-pts1: [0,0], [1,1], [2,2], [3,3], [4,4]
		k1.loadPoints(pts1);
		try {
			ArrayList<Point> Kpts = k1.chooseK(-1);
		} catch (IllegalArgumentException e) {
			String msg = "invalid k number";
			assertEquals(msg, e.getMessage());
		}
		
		// good points
		ArrayList<Point> Kpts = k1.chooseK(2);
		assertTrue(Kpts.size()==2);
		for (Point pt: Kpts) {
			assertTrue(k1.getPoints().contains(pt));
		}
	}
	
	@Test
	public void testClusterize() {
		
		// empty centroid list
		Kmean k1 = new Kmean();
		try {
			k1.clusterize(new ArrayList<Point>());
		} catch (IllegalArgumentException e) {
			String msg = "null or empty centroid list";
			assertEquals(msg, e.getMessage());
		}
		
		// good centroid list
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<5; i++) {
			pts.add(new Point(new double[] {i,i}));
		}
		k1.loadPoints(pts);
		//k1-pts: [0,0], [1,1], [2,2], [3,3], [4,4]
		ArrayList<Point> centers = new ArrayList<>();
		Point p1 = new Point(new double[] {0,0});
		Point p2 = new Point(new double[] {2,2});
		Point p3 = new Point(new double[] {3,3});
		centers.add(p1);
		centers.add(p2);
		centers.add(p3);
		ArrayList<Cluster> clusters = k1.clusterize(centers);
		assertEquals(3, clusters.size());
		Cluster c1 = new Cluster(p1);
		c1.add(new Point(new double[] {0,0}));
		Cluster c2 = new Cluster(p2);
		c2.add(new Point(new double[] {1,1}));
		c2.add(new Point(new double[] {2,2}));
		Cluster c3 = new Cluster(p3);
		c3.add(new Point(new double[] {3,3}));
		c3.add(new Point(new double[] {4,4}));
		assertEquals(c1.toString(), clusters.get(0).toString());
		assertEquals(c2.toString(), clusters.get(1).toString());
		assertEquals(c3.toString(), clusters.get(2).toString());
	}
	
	@Test
	public void testKmeanGo() {
		
		int k = 2;
		ArrayList<Point> pts = new ArrayList<>();
		Point p1 = new Point(new double[] {0});
		Point p2 = new Point(new double[] {1});
		Point p3 = new Point(new double[] {2});
		Point p4 = new Point(new double[] {3});
		pts.add(p1);
		pts.add(p2);
		pts.add(p3);
		pts.add(p4);
		//k1-pts: [0], [1], [2], [3]
		Kmean k1 = new Kmean(pts);
		k1.kMeanGo(k);
		ArrayList<Point> centroids = k1.getCentroids();
		Cluster c1 = new Cluster(new Point(new double[] {0.5}));
		c1.add(p1);
		c1.add(p2);
		Cluster c2 = new Cluster(new Point(new double[] {2.5}));
		c2.add(p3);
		c2.add(p4);
		assertEquals(c1.toString(), k1._clusters.get(0).toString());
		assertEquals(c2.toString(), k1._clusters.get(1).toString());
	}


}
