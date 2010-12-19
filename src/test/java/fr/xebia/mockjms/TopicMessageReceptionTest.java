package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasReceivedMessageOnTopic.hasReceivedMessageOnTopic;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.junit.Ignore;
import org.junit.Test;

public class TopicMessageReceptionTest {

	private static final String TOPIC_NAME = "aTopic";
	private static final String CLIENT_ID = "aClientId";

	private void receiveMessageOnTopic(Session session) throws JMSException {
		receiveMessageOnTopic(session, 1);
	}

	private void receiveMessageOnTopic(Session session, Integer numberOfMessage)
			throws JMSException {
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer = session.createConsumer(topic);
		for (Integer i = 0; i < numberOfMessage; i++) {
			consumer.receive();
		}
	}

	@Test
	public void should_receive_1_message_in_topic() throws JMSException {
		MockSession session = new MockSession();

		session.storeMessagesOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest().receiveMessageOnTopic(session);

		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME));
	}

	@Test
	public void should_receive_2_messages_in_topic() throws JMSException {
		MockSession session = new MockSession();

		session.storeMessagesOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());
		session.storeMessagesOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest().receiveMessageOnTopic(session, 2);

		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME, 2));
	}

	@Test
	public void should_receive_1_message_in_2_consumers() throws JMSException {
		MockSession session = new MockSession();
		session.storeMessagesOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage(), 2);
		new TopicMessageReceptionTest().receiveMessageOn2Consumers(session);
		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME, 2));
	}

	private void receiveMessageOn2Consumers(MockSession session)
			throws JMSException {
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer1 = session.createConsumer(topic);
		MessageConsumer consumer2 = session.createConsumer(topic);
		consumer1.receive();
		consumer2.receive();
	}

	@Test
	public void should_no_receiving_message_because_connection_not_started() {
		// TODO_TEST
	}

	@Test
	public void should_no_receiving_message_because_timeout_5s() {
		// TODO_TEST see QueueMessageReceptionTest
	}

	@Test
	@Ignore
	public void should_receiving_message_in_durable_subscriber()
			throws JMSException {
		MockSession session = new MockSession();
		session.storeMessagesOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest()
				.receiveMessageOnTopicInDurableSubscriber(session);
		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME));
	}

	private void receiveMessageOnTopicInDurableSubscriber(Session session)
			throws JMSException {
		TopicSubscriber durableSubscriber = session.createDurableSubscriber(
				new MockTopic(TOPIC_NAME), CLIENT_ID);
		durableSubscriber.receive();
	}
}
