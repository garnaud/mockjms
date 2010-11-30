package fr.xebia.mockjms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;

import fr.xebia.mockjms.exceptions.BlockingQueueException;
import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockMessageConsumer implements MessageConsumer {

	private final MockSession session;
	private final Destination destination;
	private final AtomicInteger messageReceived = new AtomicInteger(0);

	public MockMessageConsumer(MockSession session, Destination destination) {
		super();
		this.session = session;
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

	@Override
	public Message receive() throws JMSException {
		Message result = null;
		if (destination instanceof Queue) {
			Queue queue = (Queue) destination;
			result = session.popQueueStoreMessage(queue);
			if (result != null) {
				messageReceived.incrementAndGet();
			} else {
				throw new BlockingQueueException(queue.getQueueName());
			}
		}
		return result;
	}

	@Override
	public Message receive(long timeout) throws JMSException {
		MockMessage message = null;
		if (destination instanceof Queue) {
			message = session.popQueueStoreMessage((Queue) destination);
			if ((message != null) && (timeout > message.getDelayedTime())) {
				messageReceived.incrementAndGet();
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
		Message result = null;
		if (destination instanceof Queue) {
			Queue queue = (Queue) destination;
			result = session.popQueueStoreMessage(queue);
			if (result != null) {
				messageReceived.incrementAndGet();
			}
		}
		return result;
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	public int geNumberMessageOfMessagesReceived() {
		return messageReceived.get();
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

}
