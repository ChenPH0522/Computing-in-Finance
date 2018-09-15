package messages;

import static org.junit.Assert.*;

import org.junit.Test;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

public class Test_Cancel {

	@Test
	public void test_Cancel() {
		
		ClientId clientID1 = new ClientId("A");
		ClientOrderId clientOrderID1 = new ClientOrderId("1923");
		Cancel c1 = new Cancel(clientID1, clientOrderID1);
		System.out.println(c1.toString());
	}

}
