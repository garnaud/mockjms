package fr.xebia.mockjms;

import java.util.HashSet;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

public class MockConnection implements Connection {

	private final Set<MockTopicSubscriber> topicSubscribers = new HashSet<MockTopicSubscriber>();

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClientID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClientID(String clientID) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExceptionListener(ExceptionListener listener)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination,
			String messageSelector, ServerSessionPool sessionPool,
			int maxMessages) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic,
			String subscriptionName, String messageSelector,
			ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public TopicSubscriber addDurableConnection(Topic topic, String clientID) {
		MockTopicSubscriber topicSubscriber = new MockTopicSubscriber(
				topic, clientID);
		topicSubscribers.add(topicSubscriber);
		return topicSubscriber;
	}
}
