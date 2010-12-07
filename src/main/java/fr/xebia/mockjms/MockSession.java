package fr.xebia.mockjms;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockSession implements Session {

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapMessage createMapMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message createMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable object)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextMessage createTextMessage() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getTransacted() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getAcknowledgeMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void commit() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void recover() throws JMSException {
		// TODO Auto-generated method stub

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
	public void run() {
		// TODO Auto-generated method stub

	}

	ConcurrentLinkedQueue<MockMessageProducer> messageProducers = new ConcurrentLinkedQueue<MockMessageProducer>();

	@Override
	public MessageProducer createProducer(Destination destination)
			throws JMSException {
		MockMessageProducer messageProducer = new MockMessageProducer(this,
				destination);
		messageProducers.add(messageProducer);
		return messageProducer;
	}

	ConcurrentLinkedQueue<MockMessageConsumer> messageConsumers = new ConcurrentLinkedQueue<MockMessageConsumer>();

	@Override
	public MessageConsumer createConsumer(Destination destination)
			throws JMSException {
		MockMessageConsumer mockMessageConsumer = new MockMessageConsumer(this,
				destination);
		messageConsumers.add(mockMessageConsumer);
		return mockMessageConsumer;
	}

	@Override
	public MessageConsumer createConsumer(Destination destination,
			String messageSelector) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageConsumer createConsumer(Destination destination,
			String messageSelector, boolean NoLocal) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Queue createQueue(String queueName) throws JMSException {
		return new MockQueue(queueName);
	}

	@Override
	public Topic createTopic(String topicName) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name,
			String messageSelector, boolean noLocal) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String name) throws JMSException {
		// TODO Auto-generated method stub

	}

	ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>> storeQueueMessages = new ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>>();

	public void storeMessagesOnQueue(String queueName, MockMessage message) {
		if (!storeQueueMessages.containsKey(queueName)) {
			storeQueueMessages.put(queueName,
					new ConcurrentLinkedQueue<MockMessage>());
		}
		if (!storeQueueMessages.get(queueName).add(message)) {
			throw new RuntimeException();
		}
	}

	public MockMessage popQueueStoreMessage(Queue queue) {
		MockMessage mockMessage = null;
		try {
			if (storeQueueMessages.containsKey(queue.getQueueName())) {
				// Remove the message from the queue.
				mockMessage = storeQueueMessages.get(queue.getQueueName())
						.poll();
			}
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
		return mockMessage;
	}

	public MockMessageConsumer getQueueConsumer(String queueName) {
		MockMessageConsumer result = null;
		for (MockMessageConsumer messageConsumer : messageConsumers) {
			if (messageConsumer.isQueue(queueName)) {
				result = messageConsumer;
				break;
			}
		}
		return result;
	}

	public MockMessageProducer getQueueProducer(String queueName) {
		MockMessageProducer mockMessageProducer = null;
		for (MockMessageProducer messageProducer : messageProducers) {
			if (messageProducer.isQueue(queueName)) {
				mockMessageProducer = messageProducer;
				break;
			}
		}
		return mockMessageProducer;
	}

}
