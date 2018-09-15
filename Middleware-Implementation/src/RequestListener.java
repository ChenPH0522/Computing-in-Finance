package Middleware;

import javax.jms.*;

public class RequestListener implements MessageListener {

	private SimuSlave _slave;
	
	public RequestListener(SimuSlave simuSlave) {
		_slave = simuSlave;
	}

	@Override
	public void onMessage(Message msg) {
		if (msg instanceof TextMessage) {
			try {
				_slave._requestList.add(((TextMessage) msg).getText().split(","));
				_slave._isWaiting = false;	
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
