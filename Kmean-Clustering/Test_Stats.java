package ClusterHW;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import junit.framework.Assert;

public class Test_Stats {

	@Test
	public void testGetVariance() {
		
		double[] array = new double[5];
		for (int i=0; i<5; i++) {array[i] = i;}
		double var = Stats.getVariance(array);
		assertEquals(2, var, 0.000001);
	}
	
	@Test
	public void testRand() {
		
		HashSet<Integer> randNum = Stats.rand(1, 100, 50);
		/*
		for (int i: randNum) {
			System.out.println(i);
		} */
		// Test Passed
	}
	
	@Test
	public void testMean() {
		
		double[] nums = new double[] {0,1,3,4,5,1,3,3,43,3,0};
		assertEquals(6, Stats.mean(nums), Point.getPrecision());
	}

}
