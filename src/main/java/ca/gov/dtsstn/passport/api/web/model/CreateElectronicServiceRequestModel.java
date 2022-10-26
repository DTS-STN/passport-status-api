package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.validation.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CreateElectronicServiceRequest")
@JsonDeserialize(as = ImmutableCreateElectronicServiceRequestModel.class)
public interface CreateElectronicServiceRequestModel extends Serializable {

	@Nullable
	@NotBlank(message = "dateOfBirth is required; it must not be null or blank")
	@Date(message = "dateOfBirth must be a valid ISO 8601 date format (yyyy-mm-dd)")
	@Size(min = 10, max = 10, message = "dateOfBirth must be exactly 10 characters")
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", implementation = LocalDate.class, required = true)
	String getDateOfBirth();

	@Nullable
	@Email(message = "email must be a valid email address")
	@NotNull(message = "email is required; it must not be null")
	@Size(max = 256, message = "email must be 256 characters or less")
	@Pattern(message = "email must be a valid email address", regexp = "[^@]+@[^@]+\\.[^@]+") // prevents user@localhost style emails
	@Schema(description = "The email address of the user submitting the electronic service request.", example = "user@example.com", required = true)
	String getEmail();

	@Nullable
	@Size(max = 128, message = "firstName must be 128 characters or less")
	@NotBlank(message = "firstName is required; it must not be null or blank")
	@Schema(description = "The first name of the passport applicant.", example = "John", required = true)
	String getFirstName();

	@Nullable
	@Size(max = 128, message = "lastName must be 128 characters or less")
	@NotBlank(message = "lastName is required; it must not be null or blank")
	@Schema(description = "The last name of the passport applicant.", example = "Doe", required = true)
	String getLastName();

}
