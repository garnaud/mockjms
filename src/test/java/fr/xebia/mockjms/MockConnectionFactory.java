package fr.xebia.mockjms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class MockConnectionFactory implements ConnectionFactory {

	@Override
	public Connection createConnection() throws JMSException {
		return new MockConnection();
	}

	@Override
	public Connection createConnection(String userName, String password)
			throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
