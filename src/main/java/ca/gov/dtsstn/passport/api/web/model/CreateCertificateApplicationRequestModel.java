package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
@Style(validationMethod = ValidationMethod.NONE)
@Schema(name = "CreateCertificateApplicationRequest")
@JsonDeserialize(as = ImmutableCreateCertificateApplicationRequestModel.class)
public interface CreateCertificateApplicationRequestModel extends Serializable {

	@JsonProperty("CertificateApplication")
	@NotNull(message = "CertificateApplication is required; it must not be null")
	CertificateApplicationModel getCertificateApplication();

}
