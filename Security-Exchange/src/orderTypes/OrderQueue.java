package orderTypes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import orderSpecs.ClientOrderId;

public class OrderQueue extends LinkedHashMap<ClientOrderId, RestingOrder> {

	private static final long serialVersionUID = 8172160536203305983L;

	public ArrayList<RestingOrder> getOrders() {return new ArrayList<RestingOrder> (this.values());}
}
