package orderSpecs;

public class ClientOrderId {
	
	public final String _ID;
	
	public ClientOrderId(String id) {_ID = id;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_ID == null) ? 0 : _ID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientOrderId other = (ClientOrderId) obj;
		if (_ID == null) {
			if (other._ID != null)
				return false;
		} else if (!_ID.equals(other._ID))
			return false;
		return true;
	}

	@Override
	public String toString() {return "ClientOrderId [_ID=" + _ID + "]";}
}
