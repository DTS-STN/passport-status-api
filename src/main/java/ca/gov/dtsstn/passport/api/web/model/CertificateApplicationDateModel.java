package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.validation.Date;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationDate")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationDateModel.class)
public interface CertificateApplicationDateModel extends Serializable {

	@JsonProperty("Date")
	@NotBlank(message = "Date is required; it must not be null or blank")
	@Date(message = "Date must be a valid ISO 8601 date format (yyyy-mm-dd)")
	@Schema(description = "The date the certificate application was created in ISO 8601 format.", example = "2020-01-01", implementation = LocalDate.class)
	String getDate();

}
