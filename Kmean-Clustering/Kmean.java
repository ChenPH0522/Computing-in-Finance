package ClusterHW;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Kmean {
	
	private static Distance DISTANCE = Point.getDistance();
	private static double PRECISION = Point.getPrecision();
	// within this precision, points are considered the same
	private ArrayList<Point> _points;
	public ArrayList<Cluster> _clusters;
	// ture if the current clusters are the desired clusters of the current points

	// getters
	public static Distance getDistance() {return DISTANCE;}
	public static double getPrecision() {return PRECISION;}
	public ArrayList<Point> getPoints() {return _points;}
	
	// setters
	public static void setDistance(Distance newDistance) {
		DISTANCE = newDistance;
		Point.setDistance(DISTANCE);
	}
	public static void setPrecision(double newPrecision) {
		PRECISION = newPrecision;
		Point.setPrecision(newPrecision);
	}
	public void loadPoints(ArrayList<Point> points) {this._points = points;}

	//constructors
	public Kmean() {}
	public Kmean(ArrayList<Point> points) {loadPoints(points);}
	
	/**
	 * 
	 * @return list of centroids, collected from each cluster 
	 */
	public ArrayList<Point> getCentroids() {
		
		if (_clusters==null || _clusters.size()==0) {
			return null;
		}
		
		ArrayList<Point> centroids = new ArrayList<>();
		int size = _clusters.size();
		for (int i=0; i<size; i++) {
			centroids.add(_clusters.get(i).getCentroid());
		}
		return centroids;
	}
	
	/**
	 * randomly select k points as centroids, and construct empty clusters using the k points
	 * @param k
	 */
	public ArrayList<Point> chooseK(int k) {
		
		if (_points==null) {throw new IllegalArgumentException("points fewer than k");}
		int size = _points.size();
		if (size < k) {throw new IllegalArgumentException("points fewer than k");}
		if (k<=0) {throw new IllegalArgumentException("invalid k number");}
		
		HashSet<Integer> index = Stats.rand(0, size-1, k);
		ArrayList<Point> centroids = new ArrayList<>();
		for (int i: index) {centroids.add(_points.get(i));}
		
		return centroids;
	}
	
	/**
	 * set all points free
	 */
	public void freeAllPoints() {
		for (Point p: _points) {p.setTaken(false);}
	}
	
	/**
	 * 
	 * @param centroids
	 * @return the clusters that uses the input list as their centroids 
	 */
	public ArrayList<Cluster> clusterize(ArrayList<Point> centroids) {
		
		if (centroids==null || centroids.size()==0) {
			throw new IllegalArgumentException("null or empty centroid list");
		}
		
		this.freeAllPoints();
		ArrayList<Cluster> clusters = new ArrayList<>();
		HashMap<Point, Cluster> map = new HashMap<>();
		int i = 0;
		for (Point c: centroids) {
			clusters.add(new Cluster(c));
			map.put(c, clusters.get(i));
			i++;
		}
		
		Point tmp = new Point();
		for (Point p: _points) {
			tmp = p.closestPoint(centroids);
			map.get(tmp).add(p);
		}
		
		return clusters;
	}
	
	/**
	 * automatically does k-mean clustering to the loaded points
	 * @param k number of clusters
	 */
	public void kMeanGo(int k) {
		
		// converge method
		ArrayList<Point> centroids = chooseK(k);
		ArrayList<Point> centroids2 = new ArrayList<>();
		boolean converge = false;
		
		while (!converge) {
			
			_clusters = clusterize(centroids);
			for (Cluster clstr: _clusters) {clstr.update();}
			centroids2 = getCentroids();
			
			if (Point.maxDistance(centroids, centroids2) < Point.getPrecision()) {
				converge = true;
			}
			centroids = centroids2;
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
		
		Kmean k1 = new Kmean(pts);
		k1.kMeanGo(500);
		ArrayList<Point> centroids = k1.getCentroids();
		
		PrintWriter writer = new PrintWriter(
				"C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\Cluster HW\\K-Mean results.txt", "UTF-8");
		writer.println("The centroids are:");
		for (Point c: centroids) {
			writer.println(c.toString());
		}		
		writer.close();
	}
}