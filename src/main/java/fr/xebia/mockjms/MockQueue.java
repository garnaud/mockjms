package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.Queue;

import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockQueue implements Queue, MockDestination {

	private final String queueName;

	public MockQueue(String queueName) {
		super();
		this.queueName = queueName;
	}

	@Override
	public String getQueueName() throws JMSException {
		return queueName;
	}

	@Override
	public String getName() {
		try {
			return getQueueName();
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
	}

	@Override
	public boolean isQueue() {
		return true;
	}

	@Override
	public boolean isTopic() {
		return false;
	}

}
