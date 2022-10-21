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
public class CertificateApplication implements Serializable {

	@JsonProperty("CertificateApplicationApplicant")
	@NotNull(message = "CertificateApplicationApplicant is required; it must not be null")
	private CertificateApplicationApplicant certificateApplicationApplicant;

	@JsonProperty("CertificateApplicationDate")
	@NotNull(message = "CertificateApplicationDate is required; it must not be null")
	private CertificateApplicationDate certificateApplicationDate;

	@JsonProperty("CertificateApplicationIdentification")
	@NotNull(message = "CertificateApplicationIdentification is required; it must not be null")
	@Size(min = 2, max = 2, message = "CertificateApplicationIdentification must be an array with the exactly [IdentificationCategoryText='Application Register SID'] and [IdentificationCategoryText='File Number']")
	@Schema(example = "[{\"IdentificationCategoryText\": \"Application Register SID\", \"IdentificationID\": \"ABCD1234\" },{ \"IdentificationCategoryText\": \"File Number\", \"IdentificationID\": \"ABCD1234\" }]")
	private List<CertificateApplicationIdentification> certificateApplicationIdentifications;

	@JsonProperty("CertificateApplicationStatus")
	@NotNull(message = "CertificateApplicationStatus is required; it must not be null")
	private CertificateApplicationStatus certificateApplicationStatus;

	public CertificateApplicationApplicant getCertificateApplicationApplicant() {
		return certificateApplicationApplicant;
	}

	public void setCertificateApplicationApplicant(CertificateApplicationApplicant certificateApplicationApplicant) {
		this.certificateApplicationApplicant = certificateApplicationApplicant;
	}

	public CertificateApplicationDate getCertificateApplicationDate() {
		return certificateApplicationDate;
	}

	public void setCertificateApplicationDate(CertificateApplicationDate certificateApplicationDate) {
		this.certificateApplicationDate = certificateApplicationDate;
	}

	public List<CertificateApplicationIdentification> getCertificateApplicationIdentifications() {
		return certificateApplicationIdentifications;
	}

	public void setCertificateApplicationIdentifications(
			List<CertificateApplicationIdentification> certificateApplicationIdentifications) {
		this.certificateApplicationIdentifications = certificateApplicationIdentifications;
	}

	public CertificateApplicationStatus getCertificateApplicationStatus() {
		return certificateApplicationStatus;
	}

	public void setCertificateApplicationStatus(CertificateApplicationStatus certificateApplicationStatus) {
		this.certificateApplicationStatus = certificateApplicationStatus;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
