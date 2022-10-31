package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusSearchEvent.Builder;

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
	LocalDate getDateOfBirth();

	@Nullable
	String getEmail();

	@Nullable
	String getFileNumber();

	@Nullable
	String getFirstName();

	@Nullable
	String getLastName();

	Result getResult();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
