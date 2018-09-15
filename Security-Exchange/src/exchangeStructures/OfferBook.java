package exchangeStructures;

import java.util.Comparator;
import java.util.Iterator;

import fills.Comms;
import fills.Fill;
import messages.RestingOrderConfirmation;
import orderSpecs.ClientOrderId;
import orderSpecs.Price;
import orderSpecs.Quantity;
import orderTypes.OrderQueue;
import orderTypes.RestingOrder;
import orderTypes.SweepingOrder;
import util.*;

public class OfferBook extends Book {

	private static final long serialVersionUID = 576688373968567299L;

	// constructor
	public OfferBook() {
		super(11, new Comparator<Pair<Price, OrderQueue>>(){
			@Override
			public int compare(Pair o1, Pair o2) {
				return ((Price)o1.getFirstObject()).compareTo(((Price)o2.getFirstObject()));
			}
		});	
	}
	public OfferBook(int initialCapacity) {
		super(initialCapacity, new Comparator<Pair<Price, OrderQueue>>(){
			@Override
			public int compare(Pair o1, Pair o2) {
				return ((Price)o1.getFirstObject()).compareTo(((Price)o2.getFirstObject()));
			}
		});
	}
	
	/**
	 * @param swpOrder	sweepingOrder that sweeps into the OfferBook
	 * @return Comms, which stores the sweeping results, including which RestingOrders are swept
	 * and how much of the sweepingOrder is not filled and becomes a resting order.
	 */
	public Comms sweep(SweepingOrder swpOrder) {
		
		Comms comms = new Comms();
		Pair<Price, OrderQueue> pair = this.peek();
		
		// reference variables: save memory, more readable 
		OrderQueue q;
		Iterator<ClientOrderId> it;
		RestingOrder order;
		ClientOrderId orderId;
		RestingOrderConfirmation roc;
		long n;
		
		while (pair != null && 
				swpOrder.getQuantity().compareTo(new Quantity(0))>0 &&
				swpOrder.getPrice().compareTo(pair.getFirstObject()) >= 0) {
			// loop while:
			// the book is not empty
			// the sweepingOrder has positive quantity
			// can still find a price match for the sweepingOrder
			
			q = this.peek().getSecondObject();	// next OrderQueue
			it = q.keySet().iterator();
			while (it.hasNext()) {
				// iterate through Book
				// since Book is a LinkedHashMap, the order is the time each order is added
				
				orderId = it.next();
				order = q.get(orderId);
				
				// if an order is canceled - remove
				if (order.isCancelled()) {it.remove(); continue;}
				
				// if sweepingOrder has larger quantity
				if (order.getQuantity().compareTo(swpOrder.getQuantity()) < 0) {
					
					// generate Fill for the restingOrder
					comms.getFills().add(new Fill(
							order.getClientId(),
							order.getClientOrderId(),
							swpOrder.getClientId(),
							order.getQuantity()));
					
					// generate Fill for the sweepingOrder
					comms.getFills().add(new Fill(
							swpOrder.getClientId(),
							swpOrder.getClientOrderId(),
							order.getClientId(),
							order.getQuantity()));

					n = swpOrder.getQuantity().get_Quantity() - order.getQuantity().get_Quantity();
					swpOrder.setQuantity(new Quantity(n));	// update quantity
					it.remove();	// remove the swept restingOrder
					continue;
				}
				
				// if restingOrder has larger Quantity
				
				// generate Fill for the restingOrder
				comms.getFills().add(new Fill(
						order.getClientId(),
						order.getClientOrderId(),
						swpOrder.getClientId(),
						swpOrder.getQuantity()));
				
				// generate Fill for the sweepingOrder
				comms.getFills().add(new Fill(
						swpOrder.getClientId(),
						swpOrder.getClientOrderId(),
						order.getClientId(),
						swpOrder.getQuantity()));
				
				// generate RestingOrderConfirmation
				n = order.getQuantity().get_Quantity() - swpOrder.getQuantity().get_Quantity();
				order.setQuantity(new Quantity(n));
				roc = new RestingOrderConfirmation(order);
				comms.getRestingOrderConfirmations().add(roc);
				
				swpOrder.setQuantity(new Quantity(0));
				roc = new RestingOrderConfirmation(new RestingOrder(swpOrder));
				comms.getRestingOrderConfirmations().add(roc);
				
				// return immediately once the sweeping order is filled
				return comms;
			}
			this.poll();	// remove the swept price level 
			pair = this.peek();
		}
		
		// create RestingOrderConfirmation for the sweepingOrder
		roc = new RestingOrderConfirmation(new RestingOrder(swpOrder));
		comms.getRestingOrderConfirmations().add(roc);
		return comms;
	}
}
