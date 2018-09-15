package Middleware;

import static org.junit.Assert.*;

import org.junit.Test;

public class Test_StringSplit {

	@Test
	public void test() {
		String str = "Hello,World!";
		String[] parsed = str.split(",");
		String[] compare = new String[2];
		compare[0] = "Hello";
		compare[1] = "World!";
		for (int i=0; i<2; i++) {
			assertEquals(compare[i], parsed[i]);
		}
	}

}
