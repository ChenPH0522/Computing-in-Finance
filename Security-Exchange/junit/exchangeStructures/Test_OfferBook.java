package exchangeStructures;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import fills.Comms;
import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;
import orderTypes.OrderQueue;
import orderTypes.RestingOrder;
import orderTypes.SweepingOrder;
import util.Pair;

public class Test_OfferBook {

	@Test
	public void test_OfferBook() {
		Price p1_1 = new Price(7);
		Price p1_2 = new Price(1);
		Price p1_3 = new Price(300000);
		
		OfferBook b1 = new OfferBook();
		b1.add(new Pair<Price, OrderQueue>(p1_1, null));
		b1.add(new Pair<Price, OrderQueue>(p1_2, null));
		b1.add(new Pair<Price, OrderQueue>(p1_3, null));

		HashMap<Price, OrderQueue> hp1 = b1.getPriceLevels();
		System.out.println(hp1.getClass());
		for (Price p: hp1.keySet()) {
			System.out.println(p.toString());
		}
		
		for (int i = 0; i<3; i++) {
			System.out.println(b1.poll().getFirstObject().toString());
		}
	}
	
	@Test
	public void test_Sweep() {
		
		MarketId marketId0 = new MarketId( "IBM" );
		
		ClientId clientId0 = new ClientId( "Lee" );
		ClientOrderId clientOrderId0 = new ClientOrderId( "ABC" );
		Side side0 = Side.SELL;
		Quantity quantity0 = new Quantity( 1000L );
		Price price0 = new Price( 1280000 );
		RestingOrder restingOrder0 = new RestingOrder(
			clientId0,
			clientOrderId0,
			marketId0,
			side0,
			quantity0,
			price0
		);
		OrderQueue q0 = new OrderQueue();
		q0.put(clientOrderId0, restingOrder0);
		Pair<Price, OrderQueue> p0 = new Pair<>(price0, q0);
		restingOrder0.cancel();
		
		ClientId clientId1 = new ClientId( "Bob" );
		ClientOrderId clientOrderId1 = new ClientOrderId( "VZFZF" );
		MarketId marketId1 = new MarketId( "IBM" );
		Side side1 = Side.SELL;
		Quantity quantity1 = new Quantity( 500L ); // Half of the 1000 that's already in the book
		Price price1 = new Price( 1200000L );
		RestingOrder restingOrder1 = new RestingOrder(
			clientId1,
			clientOrderId1,
			marketId1,
			side1,
			quantity1,
			price1
		);
		OrderQueue q1 = new OrderQueue();
		q1.put(clientOrderId1, restingOrder1);
		Pair<Price, OrderQueue> p1 = new Pair<>(price1, q1);
		
		ClientId clientId2 = new ClientId( "Steve" );
		ClientOrderId clientOrderId2 = new ClientOrderId( "UnP17az" );
		MarketId marketId2 = new MarketId( "IBM" );
		Side side2 = Side.SELL;
		Quantity quantity2 = new Quantity( 300 );
		Price price2 = new Price( 1270000 );
		RestingOrder restingOrder2 = new RestingOrder(
			clientId2,
			clientOrderId2,
			marketId2,
			side2,
			quantity2,
			price2
		);
		OrderQueue q2 = new OrderQueue();
		q2.put(clientOrderId2, restingOrder2);
		Pair<Price, OrderQueue> p2 = new Pair<>(price2, q2);
		
		ClientId clientId3 = new ClientId( "Eric");
		ClientOrderId clientOrderId3 = new ClientOrderId( "BqUCC5" );
		MarketId marketId3 = new MarketId( "IBM" );
		Side side3 = Side.BUY;
		Quantity quantity3 = new Quantity( 1500 );
		Price price3 = new Price( 1290000 );
		SweepingOrder sweepingOrder3 = new SweepingOrder(
			clientId3,
			clientOrderId3,
			marketId3,
			side3,
			quantity3,
			price3
		);
		
		OfferBook ofb1 = new OfferBook();
		ofb1.add(p0);
		ofb1.add(p1);
		ofb1.add(p2);
		
		Comms comms = ofb1.sweep(sweepingOrder3);
		
		assertEquals(4, comms.getFills().size());
		assertEquals(700, comms.getRestingOrderConfirmations().getLast().getQuantity().get_Quantity());
		assertEquals(300, comms.getFills().getLast().getQuantity().get_Quantity());
		System.out.println(comms.getFills().getLast().toString());
	}
}
