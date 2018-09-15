package exchangeStructures;

import java.util.HashMap;

import fills.*;
import messages.*;
import orderSpecs.*;
import orderTypes.*;

public class Exchange {

	private HashMap<MarketId, Market> _marketsMap = new HashMap<MarketId, Market>();
	private HashMap<ClientOrderId, RestingOrder> _restingOrderMap = new HashMap<ClientOrderId, RestingOrder>();
	private Comms _comms = new Comms();
	
	// getter & setter 
	public HashMap<MarketId, Market> getMarketsMap() {return _marketsMap;}
	public Market getMarket(MarketId marketId) {return _marketsMap.get(marketId);}
	public HashMap<ClientOrderId, RestingOrder> getRestingOrderMap() {return _restingOrderMap;}
	public RestingOrder getOrder(ClientOrderId clientOrderId) {return _restingOrderMap.get(clientOrderId);}
	public Comms getComms() {return _comms;}

	// no constructor - use default
	
	/**
	 * @param market	market to be added
	 * @throws Exception
	 * Throw exception if the exchange associated with the market does not match to this exchange. 
	 * Throw exception if the market already exist in the exchange.
	 */
	public void addMarket(Market market) throws Exception {
		
		if (this != market.getExchange()) {
			throw new Exception("Exchange associated with the market does not match to this exchange.");
		}
		
		if (_marketsMap.containsKey(market.getMarketId())) {
			throw new Exception("Market already exist.");
		}
		
		_marketsMap.put(market.getMarketId(), market);
	}
	
	/**
	 * @param sweepingOrder	sweeping order that sweeps into the exchange
	 * @throws Exception if order is placed to a nonexistent market
	 */
	public void sweep(SweepingOrder sweepingOrder) throws Exception {

		MarketId marketId = sweepingOrder.getMarketId();
		
		if (!_marketsMap.containsKey(marketId)) {
			throw new Exception("Market not in the exchange");
		}
		
		Comms comms = _marketsMap.get(marketId).sweep(sweepingOrder);
		
		// remove all filled orders
		for (Fill f : comms.getFills()) {
			_restingOrderMap.remove(f.getClientOrderId());
			_comms.getFills().add(f);
		}
		
		// add new resting orders
		for (RestingOrderConfirmation roc : comms.getRestingOrderConfirmations()) {
			if (roc.getQuantity().compareTo(new Quantity(0))>0) {
				_restingOrderMap.put(roc.getClientOrderId(), roc.getRestingOrder());
				_comms.getRestingOrderConfirmations().add(roc);
			}
		}
	}

	/**
	 * @param cancel	cancel instruction to be executed
	 */
	public void cancel(Cancel cancel) {
		
		_comms.getCancels().add(cancel);
		ClientOrderId orderId = cancel.getClientOrderId();
		
		if (_restingOrderMap.containsKey(orderId)) {
			_restingOrderMap.get(orderId).cancel();
			_comms.getCancelationConfirmations().add(
					new CancelationConfirmation(_restingOrderMap.get(orderId)));
			_restingOrderMap.remove(orderId);
		}
		
		if (!_restingOrderMap.containsKey(orderId)) {
			_comms.getCancelRejections().add(
					new CancelRejected(cancel.getClientId(), cancel.getClientOrderId()));
		}
	}
}
