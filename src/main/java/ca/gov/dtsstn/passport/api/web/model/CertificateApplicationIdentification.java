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
@Schema(name = "CertificateApplicationIdentification")
public class CertificateApplicationIdentification implements Serializable {

	@JsonProperty("IdentificationCategoryText")
	@NotNull(message = "IdentificationCategoryText is required; it must not be null")
	@Schema(description = "A human readable description of the certificate application ID entry.", example = "Application Register SID")
	private String identificationCategoryText;

	@JsonProperty("IdentificationID")
	@NotNull(message = "IdentificationID is required; it must not be null")
	@Schema(description = "The value of the certificate application ID entry.", example = "ABCD1234")
	private String identificationId;

	public String getIdentificationCategoryText() {
		return identificationCategoryText;
	}

	public void setIdentificationCategoryText(String identificationCategoryText) {
		this.identificationCategoryText = identificationCategoryText;
	}

	public String getIdentificationId() {
		return identificationId;
	}

	public void setIdentificationId(String identificationId) {
		this.identificationId = identificationId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
