package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "PersonContactInformation")
public class PersonContactInformation implements Serializable {

	@JsonProperty("ContactEmailID")
	@Email(message = "ContactEmailID must be a valid email address")
	@NotBlank(message = "ContactEmailID is required; it must not be null or blank")
	@Pattern(message = "email must be a valid email address", regexp = "[^@]+@[^@]+\\.[^@]+")
	@Schema(description = "The email address of the certificate applicant.", example = "user@example.com")
	private String contactEmailId;

	public String getContactEmailId() {
		return contactEmailId;
	}

	public void setContactEmailId(String contactEmailId) {
		this.contactEmailId = contactEmailId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
