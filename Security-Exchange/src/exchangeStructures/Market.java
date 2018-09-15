package exchangeStructures;

import fills.*;
import orderSpecs.*;
import orderTypes.*;
import util.Pair;

public class Market {

	private final MarketId _marketId;
	private final Exchange _exchange;
	private BidBook _bidBook = new BidBook();
	private OfferBook _offerBook = new OfferBook();
	
	// getter & setter
	public MarketId getMarketId() {return _marketId;}
	public Exchange getExchange() {return _exchange;}
	public BidBook getBidBook() {return _bidBook;}
	public OfferBook getOfferBook() {return _offerBook;}
	
	// constructor
	public Market(Exchange exchange, MarketId marketId) {
		_exchange = exchange;
		_marketId = marketId;
	}
	
	/**
	 * @param swpOrder sweeping order that sweeps into the market order books
	 * @return Comms, which stores the sweeping results, including which RestingOrders are swept
	 * and how much of the sweepingOrder is not filled and becomes a resting order.
	 */
	protected Comms sweep(SweepingOrder swpOrder) {
		
		// reference variables
		Comms comms = new Comms();
		RestingOrder ro;
		boolean in = false;
		
		// case: a buy order
		if (swpOrder.getSide() == Side.BUY) {
			comms = _offerBook.sweep(swpOrder);
			ro = comms.getRestingOrderConfirmations().getLast().getRestingOrder();
			
			// add to the book iff the resting order's quantity > 0
			if (ro.getQuantity().get_Quantity()>0) {
				
				// check if the price level is already in the book
				// if in - append to the existing OrderQueue
				for (Pair<Price, OrderQueue> p : _bidBook) {
					if (p.getFirstObject().equals(ro.getPrice())) {
						p.getSecondObject().put(ro.getClientOrderId(), ro);
						in = true;
						break;
					}
				}
				
				if (!in) {	// if not in - add a new price level
					OrderQueue new_q = new OrderQueue();
					new_q.put(ro.getClientOrderId(), ro);
					_bidBook.add(new Pair<>(ro.getPrice(), new_q));
				}
			}
		}
		
		// case: a sell order
		if (swpOrder.getSide() == Side.SELL) {
			comms = _bidBook.sweep(swpOrder);
			ro = comms.getRestingOrderConfirmations().getLast().getRestingOrder();
			
			// add to the book iff the resting order's quantity > 0
			if (ro.getQuantity().get_Quantity()>0) {
				
				// check if the price level is already in the book
				// if in - append to the existing OrderQueue
				for (Pair<Price, OrderQueue> p : _offerBook) {
					if (p.getFirstObject().equals(ro.getPrice())) {
						p.getSecondObject().put(ro.getClientOrderId(), ro);
						in = true;
						break;
					}
				}
				
				if (!in) {	// if not in - add a new price level
					OrderQueue new_q = new OrderQueue();
					new_q.put(ro.getClientOrderId(), ro);
					_offerBook.add(new Pair<>(ro.getPrice(), new_q));
				}
			}
		}

		return comms;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Market other = (Market) obj;
		if (_exchange == null) {
			if (other._exchange != null)
				return false;
		} else if (!_exchange.equals(other._exchange))
			return false;
		if (_marketId == null) {
			if (other._marketId != null)
				return false;
		} else if (!_marketId.equals(other._marketId))
			return false;
		return true;
	}
}
