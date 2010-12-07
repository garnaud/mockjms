package fr.xebia.mockjms.asserts;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.JMSException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessage;
import fr.xebia.mockjms.MockMessageProducer;
import fr.xebia.mockjms.MockSession;

public class HasSentMessageOnQueue extends TypeSafeMatcher<MockSession> {

	private final String queueName;
	private final Integer expectedMessageSent;
	private final MessageProperties messageProperties;

	public HasSentMessageOnQueue(String queueName,
			MessageProperties messageProperties) {
		super();
		this.queueName = queueName;
		expectedMessageSent = 1;
		this.messageProperties = messageProperties;
	}

	public HasSentMessageOnQueue(String queueName) {
		super();
		this.queueName = queueName;
		expectedMessageSent = 1;
		messageProperties = null;
	}

	public HasSentMessageOnQueue(String queueName, Integer expectedMessageSent) {
		super();
		this.queueName = queueName;
		this.expectedMessageSent = expectedMessageSent;
		messageProperties = null;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has not sent message");
	}

	@Override
	protected boolean matchesSafely(MockSession session) {
		Boolean result = Boolean.FALSE;
		MockMessageProducer messageProducer = session
				.getQueueProducer(queueName);
		int nbMessageToSend = messageProducer.geNumberOfMessagesToSend();
		result = (messageProducer != null)
				&& (nbMessageToSend == expectedMessageSent);
		if (result && (messageProperties != null)) {
			ConcurrentLinkedQueue<MockMessage> messagesToSend = messageProducer
					.getMessagesToSend();
			Boolean foundMessageWithMatchedProperties = Boolean.FALSE;
			for (MockMessage message : messagesToSend) {
				try {
					foundMessageWithMatchedProperties = (messageProperties
							.getCorrelationID() != null)
							&& messageProperties.getCorrelationID().equals(
									message.getJMSCorrelationID());
					if (foundMessageWithMatchedProperties) {
						break;
					}

				} catch (JMSException e) {
				}
			}
			result &= foundMessageWithMatchedProperties;
		}
		return result;
	}

	public static <T> Matcher<MockSession> hasSentMessageOnQueue(
			String queueName) {
		return new HasSentMessageOnQueue(queueName);
	}

	public static <T> Matcher<MockSession> hasSentMessageOnQueue(
			String queueName, Integer expectedMessageSent) {
		return new HasSentMessageOnQueue(queueName, expectedMessageSent);
	}

	public static <T> Matcher<MockSession> hasSentMessageOnQueue(
			String queueName, MessageProperties messageProperties) {
		return new HasSentMessageOnQueue(queueName, messageProperties);
	}
}
