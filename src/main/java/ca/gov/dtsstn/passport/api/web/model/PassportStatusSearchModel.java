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

	@Parameter(required = true, description = "Passport status file number")
	@NotBlank(message = "fileNumber must not be null or blank")
	String getFileNumber();

	@Parameter(required = true, description = "Passport applicant first name")
	@NotBlank(message = "firstName must not be null or blank")
	String getFirstName();

	@Parameter(required = true, description = "Passport applicant last name")
	@NotBlank(message = "lastName must not be null or blank")
	String getLastName();

	@Parameter(required = true, description = "Passport applicant date of birth (ISO Date Format yyyy-MM-dd)")
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate getDateOfBirth();

}
