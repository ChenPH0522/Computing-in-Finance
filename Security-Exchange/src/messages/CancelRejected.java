package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

public class CancelRejected extends Cancel {

	// constructor
	public CancelRejected(ClientId clientId, ClientOrderId clientOrderId) {
		super(clientId, clientOrderId);
	}
	
	
	public String toString() {
		return this.getClientOrderId().toString() + " cancelation rejected.";
	}
}
