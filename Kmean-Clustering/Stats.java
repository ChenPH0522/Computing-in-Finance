package ClusterHW;

import java.util.HashSet;

public class Stats {
	
	public static double getVariance(double[] nums) {
		
		double sum = 0;
		int k = nums.length;
		
		double avg = Stats.mean(nums);
		
		double sumSqr = 0;
		for (double n: nums) {sumSqr += (n-avg)*(n-avg);}
		
		return sumSqr/k;
	}
	
	/**
	 * generate n non-repeatable random integers between the min and the max
	 * @param min	lower bound
	 * @param max	upper bound
	 * @param number	number of random integers to generate
	 * @return
	 */
	public static HashSet<Integer> rand(int min, int max, int number) {
		
		HashSet<Integer> randNumbers = new HashSet<>();
		int i =0;
		while (randNumbers.size() < number) {
			i = (int) Math.round(min + Math.random()*(max - min));
			randNumbers.add(i);
		}
		return randNumbers;
	}
	
	public static double mean(double[] nums) {
		double sum = 0;
		int k = nums.length;
		for (double i: nums) {sum += i;}
		return sum/k;
	}
}
