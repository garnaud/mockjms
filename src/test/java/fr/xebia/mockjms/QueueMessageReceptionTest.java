package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasReceivedMessageOnQueue.hasReceivedMessageOnQueue;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.junit.Test;

import fr.xebia.mockjms.exceptions.BlockingQueueException;

public class QueueMessageReceptionTest {

	private static final String QUEUE_NAME = "queue";

	public void receiveMessage(Session session) throws JMSException {
		receiveMessageOnLoop(session, 1);
	}

	public void receiveMessageOnLoop(Session session, Integer numberOfMessage)
			throws JMSException {
		Queue createQueue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(createQueue);
		for (Integer i = 0; i < numberOfMessage; i++) {
			consumer.receive();
		}
	}

	public void receiveMessageBefore5s(Session session) throws JMSException {
		Queue createQueue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(createQueue);
		consumer.receive(5000);
	}

	public void receiveMessageNoWait(Session session) throws JMSException {
		Queue createQueue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(createQueue);
		consumer.receiveNoWait();
	}

	@Test
	public void should_receive_message_on_queue() throws JMSException {
		MockSession session = new MockSession();
		MockTextMessage message = new MessageBuilder().buildTextMessage();
		session.storeMessagesOnQueue(QUEUE_NAME, message);

		// The method to test
		new QueueMessageReceptionTest().receiveMessage(session);

		assertThat(session, hasReceivedMessageOnQueue(QUEUE_NAME));
	}

	@Test(expected = BlockingQueueException.class)
	public void should_not_receive_message() throws JMSException {
		MockSession session = new MockSession();

		// The method to test
		new QueueMessageReceptionTest().receiveMessage(session);
	}

	@Test
	public void should_receive_message_on_queue_before_5s() throws JMSException {
		MockSession session = new MockSession();
		MockTextMessage message = new MessageBuilder().buildTextMessage();
		session.storeMessagesOnQueue(QUEUE_NAME, message);

		// The method to test
		new QueueMessageReceptionTest().receiveMessageBefore5s(session);

		assertThat(session, hasReceivedMessageOnQueue(QUEUE_NAME));
	}

	@Test(expected = JMSException.class)
	public void should_throw_exception_after_5s_of_waiting_for_message()
			throws JMSException {
		MockSession session = new MockSession();
		// Simulate delayed time of 6 seconds
		MockTextMessage message = new MessageBuilder().setDelayedTimeInMs(6000)
				.buildTextMessage();
		session.storeMessagesOnQueue(QUEUE_NAME, message);

		// The method to test
		new QueueMessageReceptionTest().receiveMessageBefore5s(session);
	}

	@Test
	public void should_receive_message_on_queue_without_waiting()
			throws JMSException {
		MockSession session = new MockSession();
		MockTextMessage message = new MessageBuilder().buildTextMessage();
		session.storeMessagesOnQueue(QUEUE_NAME, message);

		// The method to test
		new QueueMessageReceptionTest().receiveMessageNoWait(session);

		assertThat(session, hasReceivedMessageOnQueue(QUEUE_NAME));
	}

	@Test
	public void should_receive_message_null_on_queue_without_waiting()
			throws JMSException {
		MockSession session = new MockSession();

		// The method to test
		new QueueMessageReceptionTest().receiveMessageNoWait(session);
		assertThat(session, not(hasReceivedMessageOnQueue(QUEUE_NAME)));
	}

	@Test
	public void should_receive_two_messages() throws JMSException {
		MockSession session = new MockSession();

		MockTextMessage message1 = new MessageBuilder().buildTextMessage();
		MockTextMessage message2 = new MessageBuilder().buildTextMessage();
		session.storeMessagesOnQueue(QUEUE_NAME, message1);
		session.storeMessagesOnQueue(QUEUE_NAME, message2);

		new QueueMessageReceptionTest().receiveMessageOnLoop(session, 2);

		assertThat(session, hasReceivedMessageOnQueue(QUEUE_NAME, 2));
	}

	@Test
	public void should_receive_two_messages_before_5s() {
		// TODO_TEST
	}

	@Test
	public void should_receive_two_messages_without_waiting() {
		// TODO_TEST
	}
}
