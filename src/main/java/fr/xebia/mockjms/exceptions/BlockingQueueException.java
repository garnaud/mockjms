package fr.xebia.mockjms.exceptions;

public class BlockingQueueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BlockingQueueException(String queueName) {
		super("The queue " + queueName
				+ " is blocked, no message is received on it.");
	}

}
