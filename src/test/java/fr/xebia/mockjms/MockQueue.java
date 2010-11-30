package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.Queue;

public class MockQueue implements Queue {

	private final String queueName;

	public MockQueue(String queueName) {
		super();
		this.queueName = queueName;
	}

	@Override
	public String getQueueName() throws JMSException {
		return queueName;
	}

}
