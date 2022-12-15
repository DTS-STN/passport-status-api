package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import ca.gov.dtsstn.passport.api.event.ImmutableNotificationSentEvent.Builder;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationSentEvent extends Serializable {

	static Builder builder() {
		return ImmutableNotificationSentEvent.builder();
	}

	String getEmail();

	String getFileNumber();

	String getGivenName();

	String getPreferredLanguage();

	String getSurname();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
