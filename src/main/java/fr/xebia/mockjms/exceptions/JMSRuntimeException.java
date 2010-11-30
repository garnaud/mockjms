package fr.xebia.mockjms.exceptions;

import javax.jms.JMSException;

public class JMSRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JMSRuntimeException(JMSException jmsException) {
		super("Wrap checked JMSException", jmsException);
	}

}
