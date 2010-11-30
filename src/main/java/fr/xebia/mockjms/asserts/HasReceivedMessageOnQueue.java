package fr.xebia.mockjms.asserts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessageConsumer;
import fr.xebia.mockjms.MockSession;

public class HasReceivedMessageOnQueue extends TypeSafeMatcher<MockSession> {

	private String queueName = null;

	public HasReceivedMessageOnQueue(String queueName) {
		super();
		this.queueName = queueName;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText(" has not received message");
	}

	@Override
	protected boolean matchesSafely(MockSession session) {
		MockMessageConsumer messageConsumer = null;
		messageConsumer = session.getQueueConsumer(queueName);
		return ((messageConsumer != null) && (messageConsumer
				.geNumberMessageOfMessagesReceived() > 0));
	}

	public static <T> Matcher<MockSession> hasReceivedMessageOnQueue(
			String queueName) {
		return new HasReceivedMessageOnQueue(queueName);
	}
}
