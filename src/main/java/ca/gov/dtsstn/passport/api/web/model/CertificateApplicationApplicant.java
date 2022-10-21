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
@Schema(name = "CertificateApplicationApplicant")
public class CertificateApplicationApplicant implements Serializable {

	@JsonProperty("BirthDate")
	@NotNull(message = "BirthDate is required; it must not be null")
	private BirthDate birthDate;

	@JsonProperty("PersonName")
	@NotNull(message = "PersonName is required; it must not be null")
	private PersonName personName;

	@JsonProperty("PersonContactInformation")
	@NotNull(message = "PersonContactInformation is required; it must not be null")
	private PersonContactInformation personContactInformation;

	public BirthDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(BirthDate birthDate) {
		this.birthDate = birthDate;
	}

	public PersonName getPersonName() {
		return personName;
	}

	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}

	public PersonContactInformation getPersonContactInformation() {
		return personContactInformation;
	}

	public void setPersonContactInformation(PersonContactInformation personContactInformation) {
		this.personContactInformation = personContactInformation;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
