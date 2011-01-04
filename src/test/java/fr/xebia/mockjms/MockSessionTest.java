package fr.xebia.mockjms;

import javax.jms.JMSException;

import org.junit.Test;

import fr.xebia.mockjms.exceptions.CreateSameDestinationException;

public class MockSessionTest {

	private static final String QUEUE_NAME = "aQueue";

	@Test(expected = CreateSameDestinationException.class)
	public void should_throw_exception_when_create_two_same_queues()
			throws JMSException {
		MockSession mockSession = new MockSession();
		mockSession.createConsumer(new MockQueue(QUEUE_NAME));
		mockSession.createConsumer(new MockQueue(QUEUE_NAME));
	}
}
