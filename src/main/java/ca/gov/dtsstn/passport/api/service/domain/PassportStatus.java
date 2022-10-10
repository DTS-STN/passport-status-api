package ca.gov.dtsstn.passport.api.service.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

/**
 * Domain object that represents a passport application status.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatus extends Serializable {

	public enum Status {

		APPROVED,
		IN_EXAMINATION,
		REJECTED;

	}

	@Nullable
	String getId();

	@Nullable
	String getCreatedBy();

	@Nullable
	Instant getCreatedDate();

	@Nullable
	String getLastModifiedBy();

	@Nullable
	Instant getLastModifiedDate();

	@Nullable
	Long getVersion();

	@Nullable
	String getApplicationRegisterSid();

	@Nullable
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate getDateOfBirth();

	@Nullable
	String getEmail();

	@Nullable
	String getFileNumber();

	@Nullable
	String getFirstName();

	@Nullable
	String getLastName();

	@Nullable
	Status getStatus();

}
