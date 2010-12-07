package fr.xebia.mockjms.asserts;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.JMSException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessage;
import fr.xebia.mockjms.MockMessageConsumer;
import fr.xebia.mockjms.MockSession;

public class HasReceivedMessageOnQueue extends TypeSafeMatcher<MockSession> {

	private final String queueName;
	private final Integer expectedMessageReceived;
	private final MessageProperties messageProperties;

	public HasReceivedMessageOnQueue(String queueName,
			MessageProperties messageProperties) {
		super();
		this.queueName = queueName;
		expectedMessageReceived = 1;
		this.messageProperties = messageProperties;
	}

	public HasReceivedMessageOnQueue(String queueName) {
		super();
		this.queueName = queueName;
		expectedMessageReceived = 1;
		messageProperties = null;
	}

	public HasReceivedMessageOnQueue(String queueName,
			Integer expectedMessageReceived) {
		super();
		this.queueName = queueName;
		this.expectedMessageReceived = expectedMessageReceived;
		messageProperties = null;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has not received message");
	}

	@Override
	protected boolean matchesSafely(MockSession session) {
		Boolean result = Boolean.FALSE;
		MockMessageConsumer messageConsumer = session
				.getQueueConsumer(queueName);
		int messageReceived = messageConsumer.geNumberOfMessagesReceived();
		result = (messageConsumer != null)
				&& (messageReceived == expectedMessageReceived);
		if (result && (messageProperties != null)) {
			ConcurrentLinkedQueue<MockMessage> messagesReceived = messageConsumer
					.getMessagesReceived();
			for (MockMessage message : messagesReceived) {
				try {
					result &= (messageProperties.getCorrelationID() != null)
							&& messageProperties.getCorrelationID().equals(
									message.getJMSCorrelationID());
				} catch (JMSException e) {
					result = false;
				}
			}
		}
		return result;
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName) {
		return new HasReceivedMessageOnQueue(queueName);
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName, Integer expectedMessageReceived) {
		return new HasReceivedMessageOnQueue(queueName, expectedMessageReceived);
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName, MessageProperties messageProperties) {
		return new HasReceivedMessageOnQueue(queueName, messageProperties);
	}
}
