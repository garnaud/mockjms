package fr.xebia.mockjms;

import static fr.xebia.mockjms.asserts.IsAcknowledged.isAcknowledged;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import javax.jms.JMSException;
import javax.jms.Message;

import org.junit.Test;

public class AcknowledgeAMessageTest {

	/**
	 * Simulate method on message reception.
	 * 
	 * @param message
	 * @throws JMSException
	 */
	public void onMessage(Message message, boolean isAcknowledged)
			throws JMSException {
		if (isAcknowledged) {
			message.acknowledge();
		}
	}

	@Test
	public void check_that_message_is_acknowledeged() throws JMSException {
		MockMessage message = new MessageBuilder().buildTextMessage();
		new AcknowledgeAMessageTest().onMessage(message, true);
		assertThat(message, isAcknowledged());
	}

	@Test
	public void check_that_message_is_not_acknowledeged() throws JMSException {
		MockMessage message = new MessageBuilder().buildTextMessage();
		new AcknowledgeAMessageTest().onMessage(message, false);
		assertThat(message, not(isAcknowledged()));
	}

}
