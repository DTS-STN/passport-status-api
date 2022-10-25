package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "BirthDate")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableBirthDateModel.class)
public interface BirthDateModel extends Serializable {

	@JsonProperty("Date")
	@PastOrPresent(message = "Date must be in the past")
	@NotNull(message = "Date is required; it must not be null")
	@Schema(description = "The birth date of the certificate applicant in ISO 8601 format.", example = "2000-01-01")
	LocalDate getDate();

}
