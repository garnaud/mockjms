package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.HasSentMessageOnQueue.hasSentMessageOnQueue;
import static fr.xebia.mockjms.asserts.MessageProperties.withProperties;
import static org.junit.Assert.assertThat;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;

public class RequestReplyTest {

	private static final String QUEUE_NAME = "aQueue";
	private static final String REPLY_QUEUE_NAME = "aReplyQueue";

	private void replyRequest(Session session, Message message)
			throws JMSException {
		String correlationID = message.getJMSCorrelationID();
		Destination replyTo = message.getJMSReplyTo();
		MockMessage replyMessage = new MessageBuilder().setCorrelationID(
				correlationID).buildTextMessage();
		session.createProducer(replyTo).send(replyMessage);
	}

	@Test
	public void should_reply_to_request() throws Exception {
		MockSession session = new MockSession();
		MockQueue replyQueue = new MockQueue(REPLY_QUEUE_NAME);

		String correlationID = "aCorrelationID";
		MockTextMessage message = new MessageBuilder().setReplyTo(replyQueue)
				.setCorrelationID(correlationID).buildTextMessage();

		session.storeMessagesOnQueue(QUEUE_NAME, message);

		new RequestReplyTest().replyRequest(session, message);

		assertThat(
				"a message has been send to aQueue but no reply has been received to the reply queue aReplyQueue with correlationID",
				session,
				hasSentMessageOnQueue(REPLY_QUEUE_NAME, withProperties()
						.correlationID(correlationID)));
	}

}
