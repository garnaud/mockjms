package fr.xebia.mockjms.asserts;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.jms.JMSException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessage;
import fr.xebia.mockjms.MockMessageConsumer;
import fr.xebia.mockjms.MockSession;

public class HasReceivedMessageOnTopic extends TypeSafeMatcher<MockSession> {

	private final String topicName;
	private final Integer expectedMessageReceived;
	private final MessageProperties messageProperties;

	public HasReceivedMessageOnTopic(String topicName,
			MessageProperties messageProperties) {
		super();
		this.topicName = topicName;
		expectedMessageReceived = 1;
		this.messageProperties = messageProperties;
	}

	public HasReceivedMessageOnTopic(String topicName) {
		super();
		this.topicName = topicName;
		expectedMessageReceived = 1;
		messageProperties = null;
	}

	public HasReceivedMessageOnTopic(String topicName,
			Integer expectedMessageReceived) {
		super();
		this.topicName = topicName;
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

		ConcurrentLinkedQueue<MockMessage> messagesReceived = getMessagesFromAllConsumers(session);

		int messageReceived = messagesReceived.size();

		result = (messageReceived == expectedMessageReceived);
		if (result && (messageProperties != null)) {
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

	public ConcurrentLinkedQueue<MockMessage> getMessagesFromAllConsumers(
			MockSession session) {
		ConcurrentLinkedQueue<MockMessage> messagesReceived = new ConcurrentLinkedQueue<MockMessage>();
		for (MockMessageConsumer messageConsumer : session
				.getTopicConsumers(topicName)) {
			messagesReceived.addAll(messageConsumer.messagesReceived);
		}
		return messagesReceived;
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnTopic(
			String topicName) {
		return new HasReceivedMessageOnTopic(topicName);
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnTopic(
			String topicName, Integer expectedMessageReceived) {
		return new HasReceivedMessageOnTopic(topicName, expectedMessageReceived);
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnTopic(
			String topicName, MessageProperties messageProperties) {
		return new HasReceivedMessageOnTopic(topicName, messageProperties);
	}
}
