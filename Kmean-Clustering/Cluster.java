package ClusterHW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Cluster {

	private Point _centroid;
	private Collection<Point> _nonCentroid = new ArrayList<>();
	// points associated with the centroid
	private boolean _updated = false;
	// false if the non-Centroid points and the centroid are not matched
	
	// getters
	public Point getCentroid() {return _centroid;}
	public Collection<Point> getNonCentroid() {return _nonCentroid;}
	public boolean isUpdated() {return _updated;}
	public int size() {return _nonCentroid.size();}
	
	// setters
	public void setCentroid(Point centroid) {
		
		if (centroid==null) { 
			_centroid=null;
		} else {
			_centroid = centroid;
		}
		this.setUpdated(false);
	}
	public void setNonCentroid(Collection<Point> nonCentroid) {
		_nonCentroid = nonCentroid;
		for (Point p: nonCentroid) {p.setTaken(true);}
		this.setUpdated(false);
	}
	public void setUpdated(boolean updated) {this._updated = updated;}
		
	// constructors
	public Cluster() {}
	public Cluster(Point centroid) {this.setCentroid(centroid);}
	public Cluster(Collection<Point> nonCentroid) {
		this.setNonCentroid(nonCentroid);
	}
	public Cluster(Point centroid, Collection<Point> nonCentroid) {
		this.setCentroid(centroid);
		this.setNonCentroid(nonCentroid);
	}

	/**
	 * 
	 * @param centroid
	 * @param nonCentroid
	 * @param updated	true if the centroid and nonCentroid mathch to each other
	 */
	public Cluster(Point centroid, Collection<Point> nonCentroid, boolean updated) {
		this.setCentroid(centroid);
		this.setNonCentroid(nonCentroid);
		this.setUpdated(updated);
	}

	/**
	 * Add a point to the point set associated with the centroid
	 * @param point
	 * @return True if the point is successfully added.
	 */
	public boolean add(Point point) {
		
		boolean add = _nonCentroid.add(point);
		if (add == true) {this.setUpdated(false); point.setTaken(true);}
		return add;
	}
	
	/**
	 * Remove a point from the point set associated with the centroid
	 * @param point
	 * @return True if the point is removed successfully.
	 */
	public boolean remove(Point point) {
		boolean remove = _nonCentroid.remove(point);
		if (remove == true) {this.setUpdated(false); point.setTaken(false);}
		return remove;
	}
	
	/**
	 * 
	 * @param point
	 * @return true if the cluster already has the point.
	 */
	public boolean hasPoint(Point point) {return _nonCentroid.contains(point);}
	
	/**
	 * release the current centroid
	 * set the cluster's centroid to null
	 * @return current centroid
	 */
	public Point releaseCentroid() {
		Point tmp = _centroid;
		_centroid = null;
		this.setUpdated(false);
		return tmp;
	}
	
	/**
	 * initialize the cluster
	 */
	public void reset() {
		for (Point p: _nonCentroid) {p.setTaken(false);}
		this.setCentroid(null);
		this.setNonCentroid(new ArrayList<Point>());
		this.setUpdated(false);
	}
	
	/**
	 * update the centroid of the cluster
	 */
	public void update() {
		_centroid = ComputeCentroid.compute(_nonCentroid);
		this.setUpdated(true);
	}
	
	@Override
	public int hashCode() {return _centroid.hashCode();}
	
	/**
	 * 
	 * @return the variance of the distances between nonCentroid points and the centroid
	 */
	public double getVar() {
		return Stats.getVariance(_centroid.getDistance(_nonCentroid));
	}
	
	/**
	 * get the average distance between nonCentroid points and the centroid
	 * @return
	 */
	public double getDist() {
		int k = _nonCentroid.size();
		double [] dist = new double[k];
		int i = 0;
		for (Point p: _nonCentroid) {
			dist[i] = p.getDistance(_centroid);
			i++;
		}
		return Stats.mean(dist);
	}
	
	@Override
	public String toString() {
		
		String str = "";
		
		try {
			str = _centroid.toString() + "; ";
		} catch (NullPointerException e) {
			str = "null; ";
		}
		
		try {
			for (Point p: _nonCentroid) {str = str + p.toString() + ", ";}
		} catch (NullPointerException e) {
			str = str + "null";
		}
		return str;
	}
}
