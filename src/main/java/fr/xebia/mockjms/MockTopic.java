package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.Topic;

public class MockTopic implements Topic {

	private String topicName = null;

	public MockTopic(String topicName) {
		super();
		this.topicName = topicName;
	}

	@Override
	public String getTopicName() throws JMSException {
		return topicName;
	}

}
