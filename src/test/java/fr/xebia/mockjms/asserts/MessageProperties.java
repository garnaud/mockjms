package fr.xebia.mockjms.asserts;

public class MessageProperties {

	private String correlationID;

	public static MessageProperties withProperties() {
		return new MessageProperties();
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public MessageProperties correlationID(String correlationID) {
		this.correlationID = correlationID;
		return this;
	}
}
