package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplication")
@JsonDeserialize(as = ImmutableCertificateApplicationModel.class)
public interface CertificateApplicationModel extends Serializable {


	@Valid
	@Nullable
	@JsonProperty("CertificateApplicationApplicant")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "CertificateApplicationApplicant is required; it must not be null")
	CertificateApplicationApplicantModel getCertificateApplicationApplicant();

	@Valid
	@Nullable
	@JsonProperty("CertificateApplicationDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "CertificateApplicationDate is required; it must not be null")
	CertificateApplicationDateModel getCertificateApplicationDate();

	@Valid
	@Nullable
	@JsonView({ Authorities.AuthenticatedView.class })
	@JsonProperty("CertificateApplicationIdentification")
	@NotNull(message = "CertificateApplicationIdentification is required; it must not be null")
	@Size(min = 2, max = 2, message = "CertificateApplicationIdentification must be an array with the exactly [IdentificationCategoryText='Application Register SID'] and [IdentificationCategoryText='File Number']")
	@Schema(example = "[{\"IdentificationCategoryText\": \"Application Register SID\", \"IdentificationID\": \"ABCD1234\" },{ \"IdentificationCategoryText\": \"File Number\", \"IdentificationID\": \"ABCD1234\" }]")
	List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications();

	@Valid
	@Nullable
	@JsonProperty("CertificateApplicationStatus")
	@NotNull(message = "CertificateApplicationStatus is required; it must not be null")
	CertificateApplicationStatusModel getCertificateApplicationStatus();

}
