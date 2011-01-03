package fr.xebia.mockjms;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Topic;

import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockMessageProducer implements MessageProducer {

	private final Destination destination;
	private final ConcurrentLinkedQueue<MockMessage> messagesToSend = new ConcurrentLinkedQueue<MockMessage>();

	public MockMessageProducer(Destination destination) {
		this.destination = destination;
	}

	@Override
	public void setDisableMessageID(boolean value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getDisableMessageID() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisableMessageTimestamp(boolean value) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getDisableMessageTimestamp() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDeliveryMode(int deliveryMode) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDeliveryMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPriority(int defaultPriority) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPriority() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTimeToLive(long timeToLive) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getTimeToLive() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Destination getDestination() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Message message) throws JMSException {
		if (destination instanceof MockQueue) {
			// Store the message in session to be ready to be received
			MockBroker.storeMessagesOnQueue(
					((MockQueue) destination).getQueueName(),
					(MockMessage) message);
			// Store messages sent
			messagesToSend.add((MockMessage) message);
		}
	}

	@Override
	public void send(Message message, int deliveryMode, int priority,
			long timeToLive) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(Destination destination, Message message,
			int deliveryMode, int priority, long timeToLive)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	public boolean isQueue(String queueName) {
		boolean result = false;
		try {
			result = (destination instanceof Queue)
					&& queueName.equals(((Queue) destination).getQueueName());
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
		return result;
	}

	public int geNumberOfMessagesToSend() {
		return messagesToSend.size();
	}

	public ConcurrentLinkedQueue<MockMessage> getMessagesToSend() {
		return messagesToSend;
	}

	public boolean isTopic(String topicName) {
		boolean result = false;
		try {
			result = (destination instanceof Topic)
					&& topicName.equals(((Topic) destination).getTopicName());
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
		return result;
	}

}
