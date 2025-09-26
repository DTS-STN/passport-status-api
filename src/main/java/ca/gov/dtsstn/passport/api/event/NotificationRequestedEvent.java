package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import jakarta.annotation.Nullable;

import ca.gov.dtsstn.passport.api.event.ImmutableNotificationRequestedEvent.Builder;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationRequestedEvent extends Serializable {

	static Builder builder() {
		return ImmutableNotificationRequestedEvent.builder();
	}

	@Nullable
	LocalDate getDateOfBirth();

	@Nullable
	String getEmail();

	@Nullable
	String getGivenName();

	@Nullable
	String getPreferredLanguage();

	@Nullable
	String getSurname();

	@Nullable
	String getMononym();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
