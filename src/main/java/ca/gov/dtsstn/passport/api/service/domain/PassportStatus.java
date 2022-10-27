package ca.gov.dtsstn.passport.api.service.domain;

import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Domain object that represents a passport application status.
 * <p>
 * Note: since this can be used as a search probe, all fields are {@code @Nullable}.
 * TODO :: GjB :: consider creating a search-specific object and tightening up the nullable annotations here
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutablePassportStatus.class)
public interface PassportStatus extends AbstractDomainObject {

	public enum Status { APPROVED, IN_EXAMINATION, REJECTED, UNKNOWN }

	@Nullable
	String getApplicationRegisterSid();

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

	@Nullable
	Status getStatus();

	@Nullable
	LocalDate getStatusDate();

}
