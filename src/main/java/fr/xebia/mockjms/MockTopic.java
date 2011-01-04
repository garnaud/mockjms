package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.Topic;

import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockTopic implements Topic, MockDestination {

	private final String topicName;

	public MockTopic(String topicName) {
		super();
		this.topicName = topicName;
	}

	@Override
	public String getTopicName() throws JMSException {
		return topicName;
	}

	@Override
	public String getName() {
		try {
			return getTopicName();
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
	}

	@Override
	public boolean isQueue() {
		return false;
	}

	@Override
	public boolean isTopic() {
		return true;
	}

}
