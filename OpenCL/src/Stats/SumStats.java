package Stats;

public class SumStats {

	public static double getMean(double[] arr) {
		
		int n = arr.length;
		double s = 0;
		for (double d: arr) { s += d;}
		return s/n;
	}
	
	public static float getMean(float[] arr) {
		
		int n = arr.length;
		float s = 0;
		for (float d: arr) { s += d;}
		return s/n;
	}

}
