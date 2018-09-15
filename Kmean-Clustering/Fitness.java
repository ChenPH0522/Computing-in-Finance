package ClusterHW;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Fitness {

	private static void checkInput(Kmean kMean) {
		if (kMean==null || kMean._clusters==null) {
			throw new IllegalArgumentException("Null input or null field");
		}
		
		if (kMean._clusters.isEmpty()) {
			throw new IllegalArgumentException("Empty clusters");
		}
	}
	
	/**
	 * get the average variance across all clusters in a kmean object
	 * @return double
	 */
	public static double getAvgVar(Kmean kMean) {
		
		checkInput(kMean);
		double avgVar = 0;
		int k = kMean._clusters.size();
		for (Cluster c: kMean._clusters) {
			avgVar += c.getVar();
		}
		return avgVar/k;
	}
	
	public static void main(String[] args) throws IOException {
		
		PrintWriter writer = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\Cluster HW\\Comparison results.txt", "UTF-8");
		
		//Kmean
		ArrayList<Point> k1pts = new ArrayList<>();
		for (int i=0; i<100; i++) {
			for (int j=0; j<100; j++) {
				k1pts.add(new Point(new double[] {i,j}));
			}
		}
		Kmean k1 = new Kmean(k1pts);
		
		//Kmean2
		ArrayList<Point> k2pts = new ArrayList<>();
		for (int i=0; i<100; i++) {
			for (int j=0; j<100; j++) {
				k2pts.add(new Point(new double[] {i,j}));
			}
		}
		Kmean2 k2 = new Kmean2(k2pts);
		
		for (int n=0; n<100; n++) {
			
			k1.kMeanGo(500);
			k2.Kmean2Go(500, 20);
			
			writer.print("Kmean Avg_Dist_Var:" + Fitness.getAvgVar(k1) + "; ");
			writer.print("Kmean2 Avg_Dist_Var:" + Fitness.getAvgVar(k2));
			writer.println();
			
			k1._clusters.clear();
			k2._clusters.clear();
		}
		writer.close();
	}
}
