package ClusterHW;

public class DoubleCompare {
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param precision
	 * @return if the first number is larger than the second number by
	 * at least on precision level, return 1; if the reverse, return -1; 
	 * if they are within one precision level, return 0
	 */
	public static int compare(double a, double b, double precision) {
		
		if (precision < 0) {throw new IllegalArgumentException("invalid precision");}
		if ((a-b) > precision) {return 1;}
		if ((b-a) > precision) {return -1;}
		return 0;
	}
}
