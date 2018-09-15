package messages;

import orderSpecs.ClientId;
import orderSpecs.ClientOrderId;

public class Cancel {

	private final ClientId _clientId;
	private final ClientOrderId _clientOrderId;
	
	// getters
	public ClientId getClientId() {return _clientId;}
	public ClientOrderId getClientOrderId() {return _clientOrderId;}
	
	// constructor
	public Cancel(ClientId clientId, ClientOrderId clientOrderId) {
		_clientId = clientId;
		_clientOrderId = clientOrderId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cancel other = (Cancel) obj;
		if (_clientId == null) {
			if (other.getClientId() != null)
				return false;
		} else if (!_clientId.equals(other.getClientId()))
			return false;
		if (_clientOrderId == null) {
			if (other.getClientOrderId() != null)
				return false;
		} else if (!_clientOrderId.equals(other.getClientOrderId()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.getClientOrderId().toString() + " requires cancelation.";
	}
}
