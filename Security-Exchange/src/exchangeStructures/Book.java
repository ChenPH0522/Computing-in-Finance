package exchangeStructures;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import fills.*;
import orderSpecs.*;
import orderTypes.*;
import util.*;

public abstract class Book extends PriorityQueue<Pair<Price, OrderQueue>> {
	
	private static final long serialVersionUID = 2322948007529087053L;
	
	// constructor
	protected Book() {super();}
	protected Book(int i) {super(i);}
	protected Book(int i, Comparator<Pair<Price, OrderQueue>> comparator) {
		super(i, comparator);
	}

	public HashMap<Price, OrderQueue> getPriceLevels() {
		HashMap<Price, OrderQueue> mp = new HashMap<>();
		for (Pair<Price, OrderQueue> p: this) {
			mp.put(p.getFirstObject(), p.getSecondObject());
		}
		return mp;
	}
}
