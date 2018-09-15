package orderTypes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;

public class Test_OrderQueue {

	@Test
	public void test_OrderQueue() {
		ClientId clientID1 = new ClientId("A");
		ClientOrderId clientOrderID1 = new ClientOrderId("1923");
		MarketId marketID1 = new MarketId("APPL");
		Price price1 = new Price(1000);
		Quantity quantity1 = new Quantity(100);
		Side side1 = Side.BUY;
		
		RestingOrder o1 = new RestingOrder(
				clientID1, 
				clientOrderID1, 
				marketID1, 
				side1, 
				quantity1, 
				price1);
		
		ClientId clientID2 = new ClientId("B");
		ClientOrderId clientOrderID2 = new ClientOrderId("0938");
		MarketId marketID2 = new MarketId("APPL");
		Price price2 = new Price(1000);
		Quantity quantity2 = new Quantity(100);
		Side side2 = Side.BUY;
		
		RestingOrder o2 = new RestingOrder(
				clientID2, 
				clientOrderID2, 
				marketID2, 
				side2, 
				quantity2, 
				price2);
		
		ClientId clientID3 = new ClientId("B");
		ClientOrderId clientOrderID3 = new ClientOrderId("2831");
		MarketId marketID3 = new MarketId("APPL");
		Price price3 = new Price(1000);
		Quantity quantity3 = new Quantity(100);
		Side side3 = Side.BUY;
		
		RestingOrder o3 = new RestingOrder(
				clientID3, 
				clientOrderID3, 
				marketID3, 
				side3, 
				quantity3, 
				price3);
		
		OrderQueue q1 = new OrderQueue();
		q1.put(clientOrderID1, o1);
		q1.put(clientOrderID2, o2);
		q1.put(clientOrderID3, o3);
		ArrayList<Order> a1 = new ArrayList<>();
		a1.add(o1);
		a1.add(o2);
		a1.add(o3);
		assertTrue(q1.getOrders() instanceof ArrayList);
		int i = 0;
		for (ClientOrderId id : q1.keySet()) {
			assertEquals(q1.get(id), a1.get(i));
			i++;
		}
		
		Iterator<ClientOrderId> it = q1.keySet().iterator();
		int j = 0;
		while(it.hasNext()) {
			j ++;
			ClientOrderId prev = it.next();
			System.out.println(q1.get(prev).getClientOrderId().toString());
			if (j==2) it.remove();
		}
		System.out.println(q1.size());
	}
}
