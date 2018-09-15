package orderSpecs;

public class Price implements Comparable<Price> {

	public final long _PRICE; 
	public Price(long price) {_PRICE = price;}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (_PRICE ^ (_PRICE >>> 32));
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
		Price other = (Price) obj;
		if (_PRICE != other._PRICE)
			return false;
		return true;
	}

	@Override
	public int compareTo(Price price) {
		if (_PRICE < price._PRICE) return -1;
		if (_PRICE > price._PRICE) return 1;
		return 0;
	}

	@Override
	public String toString() {return "Price [_PRICE=" + _PRICE + "]";}
}
