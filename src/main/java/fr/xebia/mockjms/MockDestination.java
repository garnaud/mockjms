package fr.xebia.mockjms;

import javax.jms.Destination;

public interface MockDestination extends Destination {

	public String getName();

	public boolean isQueue();

	public boolean isTopic();
}
