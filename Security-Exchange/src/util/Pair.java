package util;

public class Pair<Type1,Type2> {
	
	protected Type1 _o1;
	protected Type2 _o2;
	
	public Pair( Type1 o1, Type2 o2 ){
		_o1 = o1;
		_o2 = o2;
	}
	
	public Type1 getFirstObject() { return _o1; }
	
	public Type2 getSecondObject() { return _o2; }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (_o1 == null) {
			if (other._o1 != null)
				return false;
		} else if (!_o1.equals(other._o1))
			return false;
		if (_o2 == null) {
			if (other._o2 != null)
				return false;
		} else if (!_o2.equals(other._o2))
			return false;
		return true;
	}
}
