package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasReceivedMessageOnTopic.hasReceivedMessageOnTopic;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.junit.Before;
import org.junit.Test;

public class TopicMessageReceptionTest {

	private static final String TOPIC_NAME = "aTopic";
	private static final String CLIENT_ID = "aClientId";
	private MockSession session;

	@Before
	public void setUp() {
		session = new MockSession();
	}

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
		session.storeMessageOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest().receiveMessageOnTopic(session);

		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME));
	}

	@Test
	public void should_receive_2_messages_in_topic() throws JMSException {
		session.storeMessageOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());
		session.storeMessageOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest().receiveMessageOnTopic(session, 2);

		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME, 2));
	}

	@Test
	public void should_receive_1_message_in_2_consumers() throws JMSException {
		session.storeMessageOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage(), 2);
		new TopicMessageReceptionTest().receiveMessageOn2Consumers(session);
		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME, 2));
	}

	@Test
	public void one_of_two_consumers_should_not_receive_message()
			throws JMSException {
		session.storeMessageOnTopicNotDurable(TOPIC_NAME,
				new MessageBuilder().buildTextMessage(), 1);
		new TopicMessageReceptionTest().receiveMessageOn2Consumers(session);
		assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME, 1));
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

}
