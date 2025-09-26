package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import ca.gov.dtsstn.passport.api.event.ImmutableNotificationSentEvent.Builder;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import jakarta.annotation.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationSentEvent extends Serializable {

	static Builder builder() {
		return ImmutableNotificationSentEvent.builder();
	}

	@Nullable
	String getEmail();

	@Nullable
	String getFileNumber();

	@Nullable
	String getGivenName();

	@Nullable
	PassportStatus getPassportStatus();

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
