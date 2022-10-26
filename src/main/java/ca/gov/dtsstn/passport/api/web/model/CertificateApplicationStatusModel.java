package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.validation.PassportStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationStatus")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationStatusModel.class)
public interface CertificateApplicationStatusModel extends Serializable {


	@JsonProperty("StatusCode")
	@PassportStatusCode(message = "StatusCode is invalid or unknown")
	@Size(max = 3, message = "StatusCode must be 3 characters or less")
	@NotBlank(message = "StatusCode is required; it must not null or blank")
	@Schema(description = "The certificate application status code.", example = "1")
	String getStatusCode();

	@Valid
	@Default
	@JsonProperty("StatusDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "StatusDate is required; it must not be null")
	default StatusDateModel getStatusDate() {
		return ImmutableStatusDateModel.builder().build();
	}

}
