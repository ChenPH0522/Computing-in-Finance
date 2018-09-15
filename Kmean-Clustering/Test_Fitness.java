package ClusterHW;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class Test_Fitness {

	@Test
	public void testGetVar() {
		
		Kmean k1 = new Kmean();
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<5; i++) {
			pts.add(new Point(new double[] {i,i}));
		}
		k1.loadPoints(pts);
		//k1-pts: [0,0], [1,1], [2,2], [3,3], [4,4]
		ArrayList<Point> centers = new ArrayList<>();
		centers.add(new Point(new double[] {0,0}));
		centers.add(new Point(new double[] {3,3}));
		k1._clusters = k1.clusterize(centers);
		assertEquals(0.472222, Fitness.getAvgVar(k1), Point.getPrecision());
	}
}
