package messages;

import orderTypes.Order;

public class CancelationConfirmation extends Confirmation {
	
	// constructor
	public CancelationConfirmation(Order order) {super(order);}

	public Order getCanceledOrder() {return this.getOrder();}

	@Override
	public String toString() {
		return this.getClientOrderId().toString() + " canceled.";
	}
}
