package ClusterHW;

public class Pair<Type1 extends Comparable<Type1>, Type2> implements Comparable<Pair>{

	public Type1 _o1;
	public Type2 _o2;
	
	// constructors
	public Pair() {};
	public Pair(Type1 o1, Type2 o2) {_o1 = o1; _o2 = o2;}
	
	@Override
	public int compareTo(Pair pair) {
		
		if (_o1 == null && pair._o1 == null) {return 0;}
		
		if (_o1 == null || pair._o1 == null) {
			throw new IllegalArgumentException("Pairs not comparable");
		}
		
		if (!_o1.getClass().equals(pair._o1.getClass())) {
			throw new IllegalArgumentException("Pairs not comparable");
		}
		
		return _o1.compareTo((Type1) pair._o1);
	}
	
	@Override
	public boolean equals(Object pair) {
		
		if (pair instanceof Pair) {
			
			if (_o1==null || ((Pair<?, ?>) pair)._o1==null) {
				throw new IllegalArgumentException("null 1st entry");
			}
			
			if (_o2==null || ((Pair<?, ?>) pair)._o2==null) {
				throw new IllegalArgumentException("null 2nd entry");
			}
			
			return _o1.equals(((Pair<?, ?>) pair)._o1) && _o2.equals(((Pair<?, ?>) pair)._o2);
		}
		return this==pair;
	}
	
	@Override
	public int hashCode() {return _o1.hashCode();}

}
