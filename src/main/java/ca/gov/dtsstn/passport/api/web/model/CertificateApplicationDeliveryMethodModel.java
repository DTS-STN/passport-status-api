package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.validation.PassportDeliveryMethodCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationDeliveryMethod")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationDeliveryMethodModel.class)
public interface CertificateApplicationDeliveryMethodModel extends Serializable {

  @JsonProperty("DeliveryMethodCode")
	@PassportDeliveryMethodCode(message = "DeliveryMethodCode is invalid or unknown")
	@Size(max = 3, message = "DeliveryMethodCode must be 3 characters or less")
	@NotBlank(message = "DeliveryMethodCode is required; it must not null or blank")
	@Schema(description = "The certificate application delivery method code.", example = "1")
	String getDeliveryMethodCode();

}
