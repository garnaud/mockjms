package fr.xebia.mockjms.asserts;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import fr.xebia.mockjms.MockMessage;

public class IsAcknowledged extends TypeSafeMatcher<MockMessage> {

	@Override
	public void describeTo(Description description) {
		description.appendText("is not ackowledged");
	}

	@Override
	protected boolean matchesSafely(MockMessage message) {
		return ((message != null) && message.isAcknowledged());
	}

	public static <T> Matcher<MockMessage> isAcknowledged() {
		return new IsAcknowledged();
	}
}
