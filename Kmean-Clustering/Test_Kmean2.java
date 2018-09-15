package ClusterHW;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class Test_Kmean2 {

	@Test
	public void testClusterize() {
		
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<5; i++) {pts.add(new Point(new double[] {i}));}
		Kmean2 k2 = new Kmean2(pts);
		//k2-pts: [0], [1], [2], [3], [4]
		ArrayList<Point> centroids = new ArrayList<>();
		centroids.add(new Point(new double[] {2}));
		centroids.add(new Point(new double[] {1}));

		// small cluster size
		try {
			k2.clusterize(centroids, 1);
		} catch (IllegalArgumentException e) {
			String msg = "too few clusters or cluster size too small";
			assertEquals(msg, e.getMessage());
		}

		// good cluster number and size
		k2._clusters = k2.clusterize(centroids, 3);
		assertEquals(3, k2._clusters.get(0).size());
		Point p2 = new Point(new double[] {1});
		Point p3 = new Point(new double[] {2});
		Point p4 = new Point(new double[] {3});
		
		Cluster c1 = new Cluster(p3);
		c1.add(p3);
		c1.add(p2);
		c1.add(p4);
		assertEquals(c1.toString(), k2._clusters.get(0).toString());
	}
	
	@Test
	public void testKmean2Go() {
		
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<5; i++) {pts.add(new Point(new double[] {i}));}
		Kmean2 k2 = new Kmean2(pts);
		//k2-pts: [0], [1], [2], [3], [4]
		
		// too small cluster size
		try {
			k2.Kmean2Go(2, 2);
		} catch (IllegalArgumentException e) {
			String msg = "too few clusters or cluster size too small";
			assertEquals(msg, e.getMessage());
		}
		
		// good cluster number and size
		k2.Kmean2Go(2, 3);
		// possible outcomes: 
		// {0,1} & {2,3,4} or the reverse
		// {0,1,2} & {3,4} or the reverse
		// {0,4} & {1,3} or the reverse
		Point p1 = new Point(new double[] {0});
		Point p2 = new Point(new double[] {1});
		Point p3 = new Point(new double[] {2});
		Point p4 = new Point(new double[] {3});
		Point p5 = new Point(new double[] {4});
		
		ArrayList<Point> c1 = new ArrayList<>();
		c1.add(new Point(new double[] {1}));
		c1.add(new Point(new double[] {3.5}));
		
		ArrayList<Point> c2 = new ArrayList<>();
		c2.add(new Point(new double[] {3.5}));
		c2.add(new Point(new double[] {1}));
		
		ArrayList<Point> c3 = new ArrayList<>();
		c3.add(new Point(new double[] {0.5}));
		c3.add(new Point(new double[] {3}));
		
		ArrayList<Point> c4 = new ArrayList<>();
		c4.add(new Point(new double[] {3}));
		c4.add(new Point(new double[] {0.5}));
		
		ArrayList<Point> c5 = new ArrayList<>();
		c5.add(p3);
		c5.add(p3);
		
		assertTrue(c1.toString().equals(k2.getCentroids().toString())
				|| c2.toString().equals(k2.getCentroids().toString())
				|| c3.toString().equals(k2.getCentroids().toString())
				|| c4.toString().equals(k2.getCentroids().toString())
				|| c5.toString().equals(k2.getCentroids().toString()));
	}

}
