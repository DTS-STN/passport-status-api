package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
	@JsonView({ Authorities.AuthenticatedView.class })
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

}
