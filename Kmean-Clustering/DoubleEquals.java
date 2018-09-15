package ClusterHW;

public class DoubleEquals {

	public static boolean equals(double x1, double x2, double precision) {
		
		if (precision < 0) {
			throw new IllegalArgumentException("precision cannot be negative");
		}
		
		return (x1-x2 < precision && x2-x1 < precision) || x1-x2 == 0;
	}

}
