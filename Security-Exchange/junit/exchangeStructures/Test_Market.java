package exchangeStructures;

import static org.junit.Assert.*;

import org.junit.Test;

import fills.*;
import orderSpecs.*;
import orderTypes.*;
import util.Pair;

public class Test_Market {

	@Test
	public void test_Sweep() {
		
		Exchange exchange = new Exchange();
		MarketId marketId0 = new MarketId( "IBM" );
		Market market0 = new Market(exchange, marketId0);
		
		ClientId clientId0 = new ClientId( "Lee" );
		ClientOrderId clientOrderId0 = new ClientOrderId( "ABC" );
		Side side0 = Side.BUY;
		Quantity quantity0 = new Quantity( 1000L );
		Price price0 = new Price( 1280000 );
		SweepingOrder sweepingOrder0 = new SweepingOrder(
			clientId0,
			clientOrderId0,
			marketId0,
			side0,
			quantity0,
			price0
		);
		
		Comms comms0 = market0.sweep(sweepingOrder0);
		assertEquals(0, comms0.getFills().size());
		assertEquals(1, comms0.getRestingOrderConfirmations().size());
		assertEquals(1000, comms0.getRestingOrderConfirmations().getLast().getQuantity().get_Quantity());
		assertEquals(1280000, comms0.getRestingOrderConfirmations().getLast().getPrice()._PRICE);
		
		assertEquals(0, market0.getOfferBook().size());
		assertEquals(1, market0.getBidBook().size());
		
		ClientId clientId1 = new ClientId( "Bob" );
		ClientOrderId clientOrderId1 = new ClientOrderId( "VZFZF" );
		MarketId marketId1 = new MarketId( "IBM" );
		Side side1 = Side.SELL;
		Quantity quantity1 = new Quantity( 500L ); // Half of the 1000 that's already in the book
		Price price1 = new Price( 1200000L );
		SweepingOrder sweepingOrder1 = new SweepingOrder(
			clientId1,
			clientOrderId1,
			marketId1,
			side1,
			quantity1,
			price1
		);
		
		Comms comms1 = market0.sweep(sweepingOrder1);
		
		assertEquals(2, comms1.getFills().size());
		assertEquals(2, comms1.getRestingOrderConfirmations().size());
		assertEquals(0, comms1.getRestingOrderConfirmations().getLast().getQuantity().get_Quantity());
		assertEquals(1200000, comms1.getRestingOrderConfirmations().getLast().getPrice()._PRICE);
		
		assertEquals(0, market0.getOfferBook().size());
		assertEquals(1, market0.getBidBook().size());
	}

}
