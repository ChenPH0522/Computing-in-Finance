package Middleware;

import javax.jms.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ResultListener implements MessageListener{
	
	private SimuManager _manager;
	
	public ResultListener(SimuManager simuManager) {
		_manager = simuManager;
	}
	
	@Override
	public void onMessage(Message msg) {
		if (msg instanceof StreamMessage) {
			try {
				for (int i=0; i<_manager._simuPackageSize; i++) {
					_manager._simuResults[i] = ((StreamMessage) msg).readDouble();
				}
				_manager._isWaiting = false;
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
