package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.immutables.value.Value.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing a passport status.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@JsonDeserialize(as = ImmutablePassportStatusCreateModel.class)
public interface PassportStatusCreateModel extends Serializable {

	@NotBlank(message = "applicationRegisterSid must not be null or blank")
	@Schema(description = "An externally generated natural key that uniquely identifies a passport status in the system.", example = "ABCD1234", required = true)
	String getApplicationRegisterSid();

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@NotNull(message = "dateOfBirth must not be null or blank")
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
	LocalDate getDateOfBirth();

	@NotBlank(message = "email must not be null or blank")
	@Schema(description = "The email address of the passport applicant.", example = "user@example.com")
	String getEmail();

	@NotBlank(message = "fileNumber must not be null or blank")
	@Schema(description = "The electronic service request file number.", example = "ABCD1234", required = true)
	String getFileNumber();

	@NotBlank(message = "firstName must not be null or blank")
	@Schema(description = "The first name of the passport applicant.", example = "John", required = true)
	String getFirstName();

	@NotBlank(message = "lastName must not be null or blank")
	@Schema(description = "The last name of the passport applicant.", example = "Doe", required = true)
	String getLastName();

}
