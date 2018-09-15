package ClusterHW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Point {

	private static double PRECISION = 0.0001;
	private static Distance DISTANCE = new GeometricDistance();
	public double[] _coordinates = new double[0];
	private boolean _taken = false;
	// true if the point is explicitly associated with another object
	
	// getters
	public static double getPrecision() {return PRECISION;}
	public static Distance getDistance() {return DISTANCE;}
	public boolean isTaken() {return _taken;}
	public int getDimension() {return _coordinates.length;}
	
	// setters
	public static void setPrecision(double newPrecision) {
		PRECISION = newPrecision;
		Kmean.setPrecision(newPrecision);
	}
	public static void setDistance(Distance newDistance) {
		DISTANCE = newDistance;
		Kmean.setDistance(newDistance);
	}
	public void setTaken(boolean newTaken) {_taken = newTaken;}

	// constructor
	public Point() {}
	public Point(double[] coordinates) {_coordinates = coordinates;}
	
	/**
	 * 
	 * @param point		another Point object
	 * @return distance from this point to another point
	 */
	public double getDistance(Point point) {
		return DISTANCE.computeDistance(this, point);
	}
	
	public double[] getDistance(Collection<Point> points) {
		
		double[] distances = new double[points.size()];
		int i = 0;
		for (Point p: points) {
			distances[i] = this.getDistance(p);
			i++;
		}
		return distances;
	}

	/**
	 * 
	 * @param points
	 * @return the closest point
	 */
	public Point closestPoint(Collection<Point> points) {
		
		if (points.isEmpty()) {return null;};
		
		int dim = this.getDimension();
		Point closestP = new Point(new double[dim]);
		for (int i=0; i<dim; i++) {closestP._coordinates[i] = Double.MAX_VALUE;}
		
		for (Point p: points) {
			if (this==p) {return p;}
			if (this.getDistance(p) <= this.getDistance(closestP)) {closestP = p;}
		}
		return closestP;
	}
	
	/**
	 *  returns the hashcode
	 */
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int dim = this.getDimension();
		int result = 1;
		
		for (int i=0; i<dim; i++) {
			result = (int) (prime * result + _coordinates[i] * _coordinates[i]);
		}
		return result;
	}
	
	/**
	 * 
	 * @param point
	 * @return	return true if coordinates of the points are the same. 
	 * Otherwise return false. 
	 */
	@Override
	public boolean equals(Object point) {
		
		if (point instanceof Point) {

			int dim1 = this.getDimension();
			int dim2 = ((Point) point).getDimension();
			
			if (dim1 != dim2) {return false;}
			if (dim1 == 0) {return true;}
			
			return DoubleEquals.equals(0, this.getDistance((Point) point), PRECISION);
		}
		return this==point;
	}
	
	/**
	 * 
	 * @param points
	 * @return the closest n untaken points in the list, relative to the current point
	 */
	public ArrayList<Point> closestNUntaken(Collection<? extends Point> points, int n) {
		
		if (points==null || points.isEmpty()) {
			throw new IllegalArgumentException("null or empty input");
		}

		ArrayList<Pair> sorter = new ArrayList<>();
		for (Point p: points) {sorter.add(new Pair(this.getDistance(p), p));}
		Collections.sort(sorter);
		
		ArrayList<Point> untakenPts = new ArrayList<>();
		int size = sorter.size();
		int i = 0, take = 0;
		
		while (i<size && take < n) {
			if (!((Point) sorter.get(i)._o2).isTaken()) {
				untakenPts.add((Point) sorter.get(i)._o2);
				take++;
			}
			i++;
		}
		return untakenPts;
	}
	
	/**
	 * 
	 * @param points1
	 * @param points2
	 * @return return the maximum distance between to lists of points. The distance
	 * is measured between two sequentially corresponding points
	 */
	public static double maxDistance(List<? extends Point> points1, List<? extends Point> points2) {
		
		if (points1==null || points2==null) {
			throw new IllegalArgumentException("null input");
		}
		
		int size1 = points1.size(), size2 = points2.size();
		
		if (size1 != size2) {
			throw new IllegalArgumentException("point lists of different sizes");
		}
		
		if (size1==0) {throw new IllegalArgumentException("empty list");}
		
		double maxDistance = 0, distance;
		
		for (int i=0; i<size1; i++) {
			distance = points1.get(i).getDistance(points2.get(i));
			if (distance > maxDistance) maxDistance = distance;
		}
		
		return maxDistance;
	}
	
	@Override
	public String toString() {
		
		String coord = "[";
		for (double i: _coordinates) {
			coord = coord + i + " ";
		}
		coord = coord+"]";
		return coord;
	}
}
