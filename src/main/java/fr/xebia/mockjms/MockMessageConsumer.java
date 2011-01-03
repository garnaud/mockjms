package fr.xebia.mockjms;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;

import fr.xebia.mockjms.exceptions.BlockingQueueException;
import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockMessageConsumer implements MessageConsumer {

	private final Destination destination;
	public final ConcurrentLinkedQueue<MockMessage> messagesReceived = new ConcurrentLinkedQueue<MockMessage>();

	public MockMessageConsumer(Destination destination) {
		super();
		this.destination = destination;
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

	// REFACTOR should use enum
	private boolean isQueueConsumer() {
		return destination instanceof Queue;
	}

	// REFACTOR should use enum
	private boolean isDurableTopicConsumer() {
		return this instanceof MockTopicDurableSubscriber;
	}

	@Override
	public Message receive() throws JMSException {
		MockMessage message = null;
		if (isQueueConsumer()) {
			Queue queue = (Queue) destination;
			message = MockBroker.popQueueStoreMessage(queue);
			if (message != null) {
				messagesReceived.add(message);
			} else {
				throw new BlockingQueueException(queue.getQueueName());
			}
		} else {
			message = MockBroker.popTopicStoreMessage((Topic) destination,
					isDurableTopicConsumer());
			if (message != null) {
				messagesReceived.add(message);
			}
		}
		return message;
	}

	@Override
	public Message receive(long timeout) throws JMSException {
		MockMessage message = null;
		if (destination instanceof Queue) {
			message = MockBroker.popQueueStoreMessage((Queue) destination);
			if ((message != null) && (timeout > message.getDelayedTime())) {
				messagesReceived.add(message);
			} else {
				throw new JMSException(
						"No message has been received during the last "
								+ timeout + "ms");
			}
		}
		return message;
	}

	@Override
	public Message receiveNoWait() throws JMSException {
		MockMessage message = null;
		if (destination instanceof Queue) {
			Queue queue = (Queue) destination;
			message = MockBroker.popQueueStoreMessage(queue);
			if (message != null) {
				messagesReceived.add(message);
			}
		}
		return message;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	public int geNumberOfMessagesReceived() {
		return messagesReceived.size();
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
