package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusSearchEvent.Builder;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatusSearchEvent extends Serializable {

	public enum Result { HIT, MISS, NON_UNIQUE }

	static Builder builder() {
		return ImmutablePassportStatusSearchEvent.builder();
	}

	@Nullable
	Iterable<String> getApplicationRegisterSids();

	@Nullable
	LocalDate getDateOfBirth();

	@Nullable
	String getEmail();

	@Nullable
	String getFileNumber();

	@Nullable
	String getGivenName();

	@Nullable
	String getSurname();

	Result getResult();

	@Nullable
	PassportStatus getPassportStatus();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
