package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "CertificateApplication")
public class CertificateApplicationModel implements Serializable {

	@JsonProperty("CertificateApplicationApplicant")
	@NotNull(message = "CertificateApplicationApplicant is required; it must not be null")
	private CertificateApplicationApplicantModel certificateApplicationApplicant;

	@JsonProperty("CertificateApplicationDate")
	@NotNull(message = "CertificateApplicationDate is required; it must not be null")
	private CertificateApplicationDateModel certificateApplicationDate;

	@JsonProperty("CertificateApplicationIdentification")
	@NotNull(message = "CertificateApplicationIdentification is required; it must not be null")
	@Size(min = 2, max = 2, message = "CertificateApplicationIdentification must be an array with the exactly [IdentificationCategoryText='Application Register SID'] and [IdentificationCategoryText='File Number']")
	@Schema(example = "[{\"IdentificationCategoryText\": \"Application Register SID\", \"IdentificationID\": \"ABCD1234\" },{ \"IdentificationCategoryText\": \"File Number\", \"IdentificationID\": \"ABCD1234\" }]")
	private List<CertificateApplicationIdentificationModel> certificateApplicationIdentifications;

	@JsonProperty("CertificateApplicationStatus")
	@NotNull(message = "CertificateApplicationStatus is required; it must not be null")
	private CertificateApplicationStatusModel certificateApplicationStatus;

	public CertificateApplicationApplicantModel getCertificateApplicationApplicant() {
		return certificateApplicationApplicant;
	}

	public void setCertificateApplicationApplicant(CertificateApplicationApplicantModel certificateApplicationApplicant) {
		this.certificateApplicationApplicant = certificateApplicationApplicant;
	}

	public CertificateApplicationDateModel getCertificateApplicationDate() {
		return certificateApplicationDate;
	}

	public void setCertificateApplicationDate(CertificateApplicationDateModel certificateApplicationDate) {
		this.certificateApplicationDate = certificateApplicationDate;
	}

	public List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications() {
		return certificateApplicationIdentifications;
	}

	public void setCertificateApplicationIdentifications(
			List<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		this.certificateApplicationIdentifications = certificateApplicationIdentifications;
	}

	public CertificateApplicationStatusModel getCertificateApplicationStatus() {
		return certificateApplicationStatus;
	}

	public void setCertificateApplicationStatus(CertificateApplicationStatusModel certificateApplicationStatus) {
		this.certificateApplicationStatus = certificateApplicationStatus;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
