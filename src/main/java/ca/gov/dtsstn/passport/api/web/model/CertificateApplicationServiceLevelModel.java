package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.validation.PassportDeliveryMethodCode;
import ca.gov.dtsstn.passport.api.web.validation.PassportServiceLevelCode;
import ca.gov.dtsstn.passport.api.web.validation.PassportStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationDeliveryMethod")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationServiceLevelModel.class)
public interface CertificateApplicationServiceLevelModel extends Serializable {

  @JsonProperty("ServiceLevelCode")
	@PassportServiceLevelCode(message = "ServiceLevelCode is invalid or unknown")
	@Size(max = 3, message = "ServiceLevelCode must be 3 characters or less")
	@NotBlank(message = "ServiceLevelCode is required; it must not null or blank")
	@Schema(description = "The certificate application service level code.", example = "1")
	String getServiceLevelCode();

}
