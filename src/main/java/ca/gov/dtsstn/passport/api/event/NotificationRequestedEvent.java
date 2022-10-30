package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import ca.gov.dtsstn.passport.api.event.ImmutableNotificationRequestedEvent.Builder;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationRequestedEvent extends Serializable {

	static Builder builder() {
		return ImmutableNotificationRequestedEvent.builder();
	}

	LocalDate getDateOfBirth();

	String getEmail();

	String getFirstName();

	String getLastName();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
