package ClusterHW;

public class GeometricDistance implements Distance{

	@Override
	public double computeDistance(Point a, Point b) {

		int dim1 = a._coordinates.length;
		int dim2 = b._coordinates.length;
		if (dim1 != dim2) {
			throw new IllegalArgumentException("points of different dimensions");
		}
		
		int i = 0;
		double sqrSum = 0;
		for (i=0; i<dim1; i++) {
			sqrSum += (a._coordinates[i] - b._coordinates[i]) *
					  (a._coordinates[i] - b._coordinates[i]);
		}
		
		double distance = Math.sqrt(sqrSum);
		if (DoubleEquals.equals(distance, 0, Point.getPrecision())) {return 0;}
		return distance;
	}

	
}
