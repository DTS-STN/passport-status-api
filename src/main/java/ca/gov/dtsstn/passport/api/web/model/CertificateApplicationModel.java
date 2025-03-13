package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.validation.CertificateApplicationIdentification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplication")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationModel.class)
public interface CertificateApplicationModel extends Serializable {

	@Valid
	@Default
	@JsonProperty("CertificateApplicationApplicant")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "CertificateApplicationApplicant is required; it must not be null")
	default CertificateApplicationApplicantModel getCertificateApplicationApplicant() {
		return ImmutableCertificateApplicationApplicantModel.builder().build();
	}

	@Valid
	@Default
	@JsonProperty("ResourceMeta")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "ResourceMeta is required; it must not be null")
	default ResourceMetaModel getResourceMeta() {
		return ImmutableResourceMetaModel.builder().build();
	}

	@Valid
	@Default
	@JsonProperty("CertificateApplicationIdentification")
	@NotNull(message = "CertificateApplicationIdentification is required; it must not be null")
	@Schema(example = "[{\"IdentificationCategoryText\": \"Application Register SID\", \"IdentificationID\": \"ABCD1234\" },{ \"IdentificationCategoryText\": \"File Number\", \"IdentificationID\": \"ABCD1234\" }]")
	@CertificateApplicationIdentification(message = "CertificateApplicationIdentification must be an array with at least [IdentificationCategoryText='Application Register SID'] and [IdentificationCategoryText='File Number']")
	default List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications() {
		return Collections.emptyList();
	}

	@Valid
	@Default
	@JsonProperty("CertificateApplicationStatus")
	@NotNull(message = "CertificateApplicationStatus is required; it must not be null")
	default CertificateApplicationStatusModel getCertificateApplicationStatus() {
		return ImmutableCertificateApplicationStatusModel.builder().build();
	}

  @Valid
	@Default
	@JsonProperty("CertificateApplicationDeliveryMethod")
	@NotNull(message = "CertificateApplicationDeliveryMethod is required; it must not be null")
	default CertificateApplicationDeliveryMethodModel getCertificateApplicationDeliveryMethod() {
		return ImmutableCertificateApplicationDeliveryMethodModel.builder().build();
	}

  @Valid
	@Default
	@JsonProperty("CertificateApplicationServiceLevel")
	@NotNull(message = "CertificateApplicationServiceLevel is required; it must not be null")
	default CertificateApplicationServiceLevelModel getCertificateApplicationServiceLevel() {
		return ImmutableCertificateApplicationServiceLevelModel.builder().build();
	}

  @Valid
	@Default
	@JsonProperty("CertificateApplicationTimelineDates")
	@NotNull(message = "CertificateApplicationTimelineDates is required; it must not be null")
	default List<CertificateApplicationTimelineDateModel> getCertificateApplicationTimelineDates() {
		return Collections.emptyList(); 
	}
}
