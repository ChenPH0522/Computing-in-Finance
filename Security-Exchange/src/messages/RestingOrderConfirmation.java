package messages;

import orderTypes.*;

public class RestingOrderConfirmation extends Confirmation {
	
	// constructor
	public RestingOrderConfirmation(RestingOrder order) {super(order);}
	
	public RestingOrder getRestingOrder() {return (RestingOrder)this.getOrder();}
	
	@Override
	public String toString() {
		return this.getClientOrderId().toString() + " gets (partially) filled.";
	}

}
