package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import ca.gov.dtsstn.passport.api.event.ImmutableNotificationNotSentEvent.Builder;
import jakarta.annotation.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationNotSentEvent extends Serializable {

	static Builder builder() {
		return ImmutableNotificationNotSentEvent.builder();
	}

	@Nullable
	Iterable<String> getApplicationRegisterSids();

	@Nullable
	LocalDate getDateOfBirth();

	@Nullable
	String getEmail();

	@Nullable
	String getGivenName();

	@Nullable
	String getSurname();

	@Nullable
	String getReason();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
