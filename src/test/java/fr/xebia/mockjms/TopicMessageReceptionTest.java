package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasReceivedMessageOnTopic.hasReceivedMessageOnTopic;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.junit.Assert;
import org.junit.Test;

public class TopicMessageReceptionTest {

	private static final String TOPIC_NAME = "aTopic";

	private void receiveMessageOnTopic(Session session) throws JMSException {
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.receive();
	}

	@Test
	public void should_receive_1_message_in_topic() throws JMSException {
		MockSession session = new MockSession();

		session.storeMessagesOnTopic(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicMessageReceptionTest().receiveMessageOnTopic(session);

		Assert.assertThat(session, hasReceivedMessageOnTopic(TOPIC_NAME));
	}

	@Test
	public void should_receive_2_messages_in_topic() {
		// TODO_TEST
	}

	@Test
	public void should_no_receiving_message_because_connection_not_started() {
		// TODO_TEST
	}

	@Test
	public void should_no_receiving_message_because_timeout_5s() {
		// TODO_TEST
	}
}
