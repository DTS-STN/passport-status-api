package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

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

	@Parameter(required = true)
	String getFileNumber();

	@Parameter(required = true)
	String getFirstName();

	@Parameter(required = true)
	String getLastName();

	@Parameter(required = true)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	LocalDate getDateOfBirth();

}
