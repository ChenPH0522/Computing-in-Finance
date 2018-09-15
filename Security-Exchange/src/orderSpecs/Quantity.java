package orderSpecs;

public class Quantity implements Comparable<Quantity>{

	private long _quantity;
	
	// getters & setters
	public long get_Quantity() {return _quantity;}
	public void set_Quantity(long quantity) {_quantity = quantity;}
	
	// constructor
	public Quantity(long l) {_quantity = l;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (_quantity ^ (_quantity >>> 32));
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
		Quantity other = (Quantity) obj;
		if (_quantity != other._quantity)
			return false;
		return true;
	}
	
	@Override
	public String toString() {return "Quantity [_quantity=" + _quantity + "]";}
	
	@Override
	public int compareTo(Quantity o) {
		if (_quantity > o.get_Quantity()) return 1;
		if (_quantity < o.get_Quantity()) return -1;
		return 0;
	}
}
