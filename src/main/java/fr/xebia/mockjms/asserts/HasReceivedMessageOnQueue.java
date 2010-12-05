package fr.xebia.mockjms.asserts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessageConsumer;
import fr.xebia.mockjms.MockSession;

public class HasReceivedMessageOnQueue extends TypeSafeMatcher<MockSession> {


	private String queueName = null;
	private Integer expectedMessageReceived=1;

	public HasReceivedMessageOnQueue(String queueName) {
		super();
		this.queueName = queueName;
	}

	public HasReceivedMessageOnQueue(String queueName,
			Integer expectedMessageReceived) {
		super();
		this.queueName = queueName;
		this.expectedMessageReceived = expectedMessageReceived;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has not received message");
	}

	@Override
	protected boolean matchesSafely(MockSession session) {
		MockMessageConsumer messageConsumer = null;
		messageConsumer = session.getQueueConsumer(queueName);
		int messageReceived = messageConsumer
				.geNumberOfMessagesReceived();
		return ((messageConsumer != null) && (messageReceived == expectedMessageReceived));
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName) {
		return new HasReceivedMessageOnQueue(queueName);
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName, Integer expectedMessageReceived) {
		return new HasReceivedMessageOnQueue(queueName,expectedMessageReceived);
	}
}
