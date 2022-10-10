package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.immutables.value.Value.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import ca.gov.dtsstn.passport.api.web.annotation.Parameter;

/**
 * REST model representing a passport status search.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatusSearchModel extends Serializable {

	@NotBlank(message = "fileNumber must not be null or blank")
	@Parameter(description = "The electronic service request file number.", example = "ABCD1234", required = true)
	String getFileNumber();

	@NotBlank(message = "firstName must not be null or blank")
	@Parameter(description = "The first name of the passport applicant.", example = "John", required = true)
	String getFirstName();

	@NotBlank(message = "lastName must not be null or blank")
	@Parameter(description = "The last name of the passport applicant.", example = "Doe", required = true)
	String getLastName();

	@NotNull(message = "dateOfBirth must not be null or blank")
	@Parameter(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate getDateOfBirth();

}
