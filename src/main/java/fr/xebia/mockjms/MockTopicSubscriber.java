package fr.xebia.mockjms;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import fr.xebia.mockjms.exceptions.BlockingQueueException;

public class MockTopicSubscriber implements TopicSubscriber {

	private String clientID = null;
	private Topic topic = null;
	private final ConcurrentLinkedQueue<MockMessage> messagesReceived = new ConcurrentLinkedQueue<MockMessage>();
	private final MockSession session;

	public MockTopicSubscriber(MockSession session, Topic topic, String clientID) {
		this.clientID = clientID;
		this.topic = topic;
		this.session = session;
	}

	@Override
	public String getMessageSelector() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMessageListener(MessageListener listener)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Message receive() throws JMSException {
		MockMessage message = null;
		Topic queue = getTopic();
		message = session.popTopicStoreMessage(queue);
		if (message != null) {
			messagesReceived.add(message);
		} else {
			throw new BlockingQueueException(queue.getTopicName());
		}
		return message;
	}

	@Override
	public Message receive(long timeout) throws JMSException {
		MockMessage message = null;
		Topic queue = getTopic();
		message = session.popTopicStoreMessage(queue);
		if (message != null) {
			messagesReceived.add(message);
		} else {
			throw new BlockingQueueException(queue.getTopicName());
		}
		return message;
	}

	@Override
	public Message receiveNoWait() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Topic getTopic() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getNoLocal() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	public Topic getTopicName() {
		return topic;
	}

	public String getClientID() {
		return clientID;
	}

	public boolean isTopic(String topicName) {
		return getTopicName().equals(topicName);
	}

	public int geNumberOfMessagesReceived() {
		return messagesReceived.size();
	}

	public ConcurrentLinkedQueue<MockMessage> getMessagesReceived() {
		return messagesReceived;
	}

}
