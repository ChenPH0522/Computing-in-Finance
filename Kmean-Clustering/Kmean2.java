package ClusterHW;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Kmean2 extends Kmean {
	
	private static double VAR_CONVERGENCE_LEVEL = Kmean.getPrecision();
	
	//getter & setter
	public double getVarConvergenceLevel() {return VAR_CONVERGENCE_LEVEL;}
	public void setVarConvergenceLevel(double level) {VAR_CONVERGENCE_LEVEL = level;}
	
	//constructors
	public Kmean2() {super();}
	public Kmean2(ArrayList<Point> points) {super(points);}
	
	/**
	 * K-mean cluster the points with specified cluster size n, with the input list as the
	 * clusters' centroids
	 * @param centroids
	 * @param n
	 * @return the clusters that uses the input list as their centroids
	 */
	public ArrayList<Cluster> clusterize(ArrayList<Point> centroids, int n) {
		
		int k = centroids.size();
		if (k*n < this.getPoints().size()) {
			throw new IllegalArgumentException("too few clusters or cluster size too small");
		}
		
		this.freeAllPoints();
		
		ArrayList<Cluster> clusters = new ArrayList<>();
		ArrayList<Point> tmp = new ArrayList<>();
		for (Point c: centroids) {
			tmp = c.closestNUntaken(this.getPoints(), n);
			clusters.add(new Cluster(c, tmp));
			c.setTaken(true);
		}
		return clusters;
	}
	
	/**
	 * Automatically does Kmean clustering, but with fixed cluster size.
	 * If the number of points is less than the specified 
	 * cluster number times cluster size, then some cluster will have smaller sizes
	 * to accomodate this
	 * @param k
	 * @param n
	 */
	public void Kmean2Go(int k, int n) {
		
		if (k*n < this.getPoints().size()) {
			throw new IllegalArgumentException("too few clusters or cluster size too small");
		}
		
		ArrayList<Point> centroids = this.chooseK(k);
		double avgVar1 = Double.MAX_VALUE;
		double avgVar2 = Double.MIN_VALUE;

		while (DoubleCompare.compare(avgVar2, avgVar1, this.getVarConvergenceLevel())<0) {
			avgVar1 = avgVar2;
			if (avgVar1 == Double.MIN_VALUE) {avgVar1 = Double.MAX_VALUE;}
			
			this._clusters = this.clusterize(centroids, n);
			for (Cluster c: this._clusters) {c.update();}
			avgVar2 = Fitness.getAvgVar(this);
			centroids = this.getCentroids();
		}
	}
	
	/*
	 * Homework Example
	 */
	public static void main(String[] args) throws IOException {
		
		ArrayList<Point> pts = new ArrayList<>();
		for (int i=0; i<100; i++) {
			for (int j=0; j<100; j++) {
				pts.add(new Point(new double[] {i,j}));
			}
		}
		
		Kmean2 k2 = new Kmean2(pts);
		k2.Kmean2Go(500, 20);
		
		ArrayList<Point> centroids = k2.getCentroids();
		
		PrintWriter writer = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\Cluster HW\\K-Mean2 results.txt", "UTF-8");
		writer.println("The centroids are:");
		for (Point c: centroids) {
			writer.println(c.toString());
		}		
		writer.close();
	}
}