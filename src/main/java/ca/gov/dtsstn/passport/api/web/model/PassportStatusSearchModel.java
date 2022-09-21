package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;

import ca.gov.dtsstn.passport.api.web.annotation.Parameter;

/**
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
	LocalDate getDateOfBirth();

}
