package orderTypes;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;

public class SweepingOrder extends Order {
	
	// constructor
	public SweepingOrder(ClientId clientId,
			ClientOrderId clientOrderId,
			MarketId marketId,
			Side side,
			Quantity quantity,
			Price price) {
		super(clientId, clientOrderId, marketId, side, quantity, price);
	}
}
