package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

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
@Schema(name = "StatusDate")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableStatusDateModel.class)
public interface StatusDateModel extends Serializable {

	@JsonProperty("Date")
	@Size(min = 10, max = 10, message = "Date must be exactly 10")
	@NotBlank(message = "Date is required; it must not be null or blank")
	@PastOrPresent(message = "Date must be a valid ISO 8601 date format (yyyy-mm-dd) and in the past")
	@Schema(description = "The birth date of the certificate applicant in ISO 8601 format.", example = "2000-01-01", implementation = LocalDate.class)
	String getDate();

}
