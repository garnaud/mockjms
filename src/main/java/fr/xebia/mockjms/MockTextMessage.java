package fr.xebia.mockjms;

import javax.jms.JMSException;
import javax.jms.TextMessage;

public class MockTextMessage extends MockMessage implements TextMessage {

	@Override
	public void setText(String string) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getText() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
