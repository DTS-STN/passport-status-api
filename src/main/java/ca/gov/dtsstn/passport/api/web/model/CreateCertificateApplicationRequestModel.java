package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
@Schema(name = "CreateCertificateApplicationRequest")
@JsonDeserialize(as = ImmutableCreateCertificateApplicationRequestModel.class)
public interface CreateCertificateApplicationRequestModel extends Serializable {

	@Valid
	@Default
	@JsonProperty("CertificateApplication")
	@NotNull(message = "CertificateApplication is required; it must not be null")
	default CertificateApplicationModel getCertificateApplication() {
		return ImmutableCertificateApplicationModel.builder().build();
	}

}
