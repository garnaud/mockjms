package fr.xebia.mockjms;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

import fr.xebia.mockjms.exceptions.JMSRuntimeException;

public class MockBroker {

	private final static ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>> storeQueueMessages = new ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>>();
	private final static ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>> storeTopicMessages = new ConcurrentHashMap<String, ConcurrentLinkedQueue<MockMessage>>();

	public static void storeMessagesOnQueue(String queueName,
			MockMessage message) {
		if (!storeQueueMessages.containsKey(queueName)) {
			storeQueueMessages.put(queueName,
					new ConcurrentLinkedQueue<MockMessage>());
		}
		if (!storeQueueMessages.get(queueName).add(message)) {
			throw new RuntimeException();
		}
	}

	public static MockMessage popQueueStoreMessage(Queue queue) {
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

	public static void storeMessageOnTopic(String topicName,
			MockTextMessage message, int countOfSubscribers) {
		if (!storeTopicMessages.containsKey(topicName)) {
			storeTopicMessages.put(topicName,
					new ConcurrentLinkedQueue<MockMessage>());
		}
		message.numberOfConsumers = new AtomicInteger(countOfSubscribers);
		if (!storeTopicMessages.get(topicName).add(message)) {
			throw new RuntimeException("Can't store message on " + topicName
					+ " (message=" + message + ")");
		}
	}

	public static void storeMessagesOnTopic(String topicName,
			MockTextMessage message) {
		storeMessageOnTopic(topicName, message, 1);
	}

	/**
	 * Store a message for a topic not durable. It means that the not durable
	 * subscriber will receive the message anyway on contrary of the JMS
	 * specification. That prevents from doing two threads, first one for
	 * waiting for the message and the second, after the subscriber is ready,
	 * for sending the message. So the behavior is totally synchronous and
	 * easier to test. Only one subscriber is authorized for this topic.
	 * 
	 * @see #storeMessageOnTopicNotDurable(String, MockMessage, int)
	 * 
	 * @param topicName
	 * @param message
	 */
	public static void storeMessageOnTopicNotDurable(String topicName,
			MockMessage message) {
		storeMessageOnTopicNotDurable(topicName, message, 1);
	}

	/**
	 * Store a message for a topic not durable. It means that the not durable
	 * subscriber will receive the message anyway on contrary of the JMS
	 * specification. That prevents from doing two threads, first one for
	 * waiting for the message and the second, after the subscriber is ready,
	 * for sending the message. So the behavior is totally synchronous and
	 * easier to test.
	 * 
	 * @see #storeMessageOnTopicNotDurable(String, MockMessage)
	 * @param topicName
	 * @param message
	 * @param countOfSubscribers
	 *            the number of subscribers which will receive the message. Once
	 *            all subscribers have receive the message, the message will be
	 *            discarded.
	 */
	public static void storeMessageOnTopicNotDurable(String topicName,
			MockMessage message, int countOfSubscribers) {
		if (!storeTopicMessages.containsKey(topicName)) {
			storeTopicMessages.put(topicName,
					new ConcurrentLinkedQueue<MockMessage>());
		}
		message.keptForNotDurableSubscriber = true;
		message.numberOfConsumers = new AtomicInteger(countOfSubscribers);
		if (!storeTopicMessages.get(topicName).add(message)) {
			throw new RuntimeException("Can't store message on " + topicName
					+ " (message=" + message + ")");
		}
	}

	public static MockMessage popTopicStoreMessage(Topic topic,
			boolean isFromDurableConsumer) {
		MockMessage mockMessage = null;
		try {
			if (storeTopicMessages.containsKey(topic.getTopicName())) {
				// Remove the message from the topic.
				for (MockMessage message : storeTopicMessages.get(topic
						.getTopicName())) {
					if (message.keptForNotDurableSubscriber
							|| isFromDurableConsumer) {
						// The message is available for all subscribers, even if
						// it subscribes after the message would be sent.
						mockMessage = message;
						message.numberOfConsumers.decrementAndGet();
						if (message.numberOfConsumers.intValue() == 0) {
							storeTopicMessages.get(topic.getTopicName())
									.remove(message);
						}
						break;
					}
				}
			}
		} catch (JMSException e) {
			throw new JMSRuntimeException(e);
		}
		return mockMessage;
	}

}
