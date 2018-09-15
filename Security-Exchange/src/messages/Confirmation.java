package messages;

import orderSpecs.*;
import orderTypes.Order;

public class Confirmation {

	private final Order _order;
	
	// getter
	public Order getOrder() {return _order;}
	public ClientId getClientId() {return _order.getClientId();}
	public ClientOrderId getClientOrderId() {return _order.getClientOrderId();}
	public MarketId getMarketId() {return _order.getMarketId();}
	public Price getPrice() {return _order.getPrice();}
	public Quantity getQuantity() {return _order.getQuantity();}
	public Side getSide() {return _order.getSide();}
	
	// constructor
	public Confirmation(Order order) {_order = order;}

}
