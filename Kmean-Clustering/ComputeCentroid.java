package ClusterHW;

import java.util.Collection;

public class ComputeCentroid {

	public static Point compute(Collection<Point> points) {
		
		int dim = getDimension(points);
		double counter = 0;
		double[] sumArray = new double[dim];
		
		for (Point p: points) {
			counter += 1;
			if (p.getDimension() != dim) {
				throw new IllegalArgumentException("point dimensions are inconsistent");
			}
			for (int i=0; i<dim; i++) {sumArray[i] += p._coordinates[i];
			}
		}
		if (dim==0) {return new Point();}
		for (int i=0; i<dim; i++) {sumArray[i] =  sumArray[i]/counter;}
		return new Point(sumArray);
	}

	private static int getDimension(Collection<Point> points) {
		
		if (points == null || points.isEmpty()) {
			throw new IllegalArgumentException("empty point list");
		}
		int dim = 0;
		for (Point p : points) {dim = p.getDimension(); break;}		
		return dim;
	}
}
