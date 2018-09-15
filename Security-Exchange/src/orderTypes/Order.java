package orderTypes;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;
import orderSpecs.MarketId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderSpecs.Side;

public class Order {
	
	private final ClientId _clientId;
	private final ClientOrderId _clientOrderId;
	private final MarketId _marketId;
	private final Side _side;
	private Quantity _quantity; 
	private final Price _price;
	private boolean _cancel = false;
	
	// getters & setters
	public ClientId getClientId() {return _clientId;}
	public ClientOrderId getClientOrderId() {return _clientOrderId;}
	public MarketId getMarketId() {return _marketId;}
	public Side getSide() {return _side;}
	public Quantity getQuantity() {return _quantity;}
	public Price getPrice() {return _price;}
	public boolean isCancelled() {return _cancel;}
	
	public void setQuantity(Quantity quantity) {_quantity = quantity;}
	public void cancel() {_cancel = true;}
	
	// constructor
	public Order(ClientId clientId,
			ClientOrderId clientOrderId,
			MarketId marketId,
			Side side,
			Quantity quantity,
			Price price) {
		_clientId = clientId;
		_clientOrderId = clientOrderId;
		_marketId = marketId;
		_side = side;
		_quantity = quantity;
		_price = price;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (_clientId == null) {
			if (other._clientId != null)
				return false;
		} else if (!_clientId.equals(other._clientId))
			return false;
		if (_clientOrderId == null) {
			if (other._clientOrderId != null)
				return false;
		} else if (!_clientOrderId.equals(other._clientOrderId))
			return false;
		if (_marketId == null) {
			if (other._marketId != null)
				return false;
		} else if (!_marketId.equals(other._marketId))
			return false;
		if (_side == null) {
			if (other._side != null)
				return false;
		} else if (!_side.equals(other._side))
			return false;
		return true;
	}
	
	// TODO consider market / partially market orders
	// TODO consider block orders
}
