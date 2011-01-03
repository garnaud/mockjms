package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasReceivedMessageOnTopic.hasReceivedMessageOnTopic;
import static org.junit.Assert.assertThat;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TopicSubscriber;

import org.junit.Before;
import org.junit.Test;

public class TopicDurableMessageReceptionTest {

	private static final String TOPIC_NAME = "aTopic";
	private static final String CLIENT_ID = "aClientId";
	private MockSession session;

	@Before
	public void setUp() {
		session = new MockSession();
	}

	@Test
	public void should_receive_message_in_durable_subscriber()
			throws JMSException {

		MockBroker.storeMessagesOnTopic(TOPIC_NAME,
				new MessageBuilder().buildTextMessage());

		new TopicDurableMessageReceptionTest()
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
