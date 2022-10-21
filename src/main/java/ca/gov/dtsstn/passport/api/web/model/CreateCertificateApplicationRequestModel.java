package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "CreateCertificateApplicationRequest")
public class CreateCertificateApplicationRequestModel implements Serializable {

	@JsonProperty("CertificateApplication")
	@NotNull(message = "CertificateApplication is required; it must not be null")
	private CertificateApplicationModel certificateApplication;

	public CertificateApplicationModel getCertificateApplication() {
		return certificateApplication;
	}

	public void setCertificateApplication(CertificateApplicationModel certificateApplication) {
		this.certificateApplication = certificateApplication;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
