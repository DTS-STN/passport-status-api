package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
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
@Schema(name = "CertificateApplication")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationModel.class)
public interface CertificateApplicationModel extends Serializable {

	@JsonProperty("CertificateApplicationApplicant")
	@NotNull(message = "CertificateApplicationApplicant is required; it must not be null")
	CertificateApplicationApplicantModel getCertificateApplicationApplicant();

	@JsonProperty("CertificateApplicationDate")
	@NotNull(message = "CertificateApplicationDate is required; it must not be null")
	CertificateApplicationDateModel getCertificateApplicationDate();

	@JsonProperty("CertificateApplicationIdentification")
	@NotNull(message = "CertificateApplicationIdentification is required; it must not be null")
	@Size(min = 2, max = 2, message = "CertificateApplicationIdentification must be an array with the exactly [IdentificationCategoryText='Application Register SID'] and [IdentificationCategoryText='File Number']")
	@Schema(example = "[{\"IdentificationCategoryText\": \"Application Register SID\", \"IdentificationID\": \"ABCD1234\" },{ \"IdentificationCategoryText\": \"File Number\", \"IdentificationID\": \"ABCD1234\" }]")
	List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications();

	@JsonProperty("CertificateApplicationStatus")
	@NotNull(message = "CertificateApplicationStatus is required; it must not be null")
	CertificateApplicationStatusModel getCertificateApplicationStatus();

}
