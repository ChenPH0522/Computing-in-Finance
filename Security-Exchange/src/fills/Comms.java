package fills;

import java.util.LinkedList;

import messages.*;

public class Comms {

	private LinkedList<Fill> _fills = new LinkedList<>();
	private LinkedList<Cancel> _cancels = new LinkedList<>();
	private LinkedList<CancelRejected> _cancelRejections = new LinkedList<>();
	private LinkedList<RestingOrderConfirmation> _restingOrderConfirmations = new LinkedList<>();
	private LinkedList<CancelationConfirmation> _cancelationConfirmations = new LinkedList<>();
	
	// getter & setter
	public LinkedList<Fill> getFills() {return _fills;}
	public LinkedList<Cancel> getCancels() {return _cancels;}
	public LinkedList<CancelRejected> getCancelRejections() {return _cancelRejections;}
	public LinkedList<RestingOrderConfirmation> getRestingOrderConfirmations() {return _restingOrderConfirmations;}
	public LinkedList<CancelationConfirmation> getCancelationConfirmations() {return _cancelationConfirmations;}
	
	// constructor
	public Comms() {super();}

}
