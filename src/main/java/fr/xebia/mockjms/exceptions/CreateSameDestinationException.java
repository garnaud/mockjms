package fr.xebia.mockjms.exceptions;

import fr.xebia.mockjms.MockDestination;

public class CreateSameDestinationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreateSameDestinationException(MockDestination destination) {
		super("Destination " + destination.getName()
				+ " is already existed in the current session");
	}
}
