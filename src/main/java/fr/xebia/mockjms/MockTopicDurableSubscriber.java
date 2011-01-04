package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class MockTopicDurableSubscriber extends MockMessageConsumer implements
		TopicSubscriber {

	private final String clientID;
	private final MockTopic topic;

	public MockTopicDurableSubscriber(MockSession session, MockTopic topic,
			String clientID) {
		super(topic);
		this.clientID = clientID;
		this.topic = topic;
	}

	@Override
	public Topic getTopic() throws JMSException {
		return topic;
	}

	@Override
	public boolean getNoLocal() throws JMSException {
		// TODO Manage no local cases
		return false;
	}

	public String getClientID() {
		return clientID;
	}

}
