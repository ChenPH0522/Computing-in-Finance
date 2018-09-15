package orderTypes;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;

public class RestingOrder extends Order {

	// constructor
	public RestingOrder(SweepingOrder sweepingOrder) {
		super(sweepingOrder.getClientId(),
				sweepingOrder.getClientOrderId(),
				sweepingOrder.getMarketId(),
				sweepingOrder.getSide(),
				sweepingOrder.getQuantity(),
				sweepingOrder.getPrice());
	}
	public RestingOrder(ClientId clientId,
			ClientOrderId clientOrderId,
			MarketId marketId,
			Side side,
			Quantity quantity,
			Price price) {
		super(clientId,
			clientOrderId,
			marketId,
			side,
			quantity,
			price);
	}
}