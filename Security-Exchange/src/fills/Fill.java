package fills;

import orderSpecs.*;

public class Fill {

	private final ClientId _thisClientId;
	private final ClientOrderId _thisClientOrderId;
	private final ClientId _counterpartyId;
	private final Quantity _quantity;
	
	// getters
	public ClientId getClientId() {return _thisClientId;}
	public ClientId getCounterpartyId() {return _counterpartyId;}
	public Quantity getQuantity() {return _quantity;}
	public ClientOrderId getClientOrderId() {return _thisClientOrderId;}

	// constructor
	public Fill(ClientId clientId,
			ClientOrderId clientOrderId,
			ClientId counterpartyId,
			Quantity quantity) {
		_thisClientId = clientId;
		_thisClientOrderId = clientOrderId;
		_counterpartyId = counterpartyId;
		_quantity = quantity;
	}
	
	public String toString() {
		return _thisClientId.toString() + " fulfilled " + _thisClientOrderId + " by " + 
				_quantity.toString() + " from " + _counterpartyId.toString();
	}
}
