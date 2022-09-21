package ca.gov.dtsstn.passport.api.service.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatus extends Serializable {

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
	String getFileNumber();

	@Nullable
	String getFirstName();

	@Nullable
	String getLastName();

	@Nullable
	LocalDate getDateOfBirth();

}
