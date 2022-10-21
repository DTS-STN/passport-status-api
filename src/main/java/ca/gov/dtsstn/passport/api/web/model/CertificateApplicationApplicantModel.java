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
public class CertificateApplicationApplicantModel implements Serializable {

	@JsonProperty("BirthDate")
	@NotNull(message = "BirthDate is required; it must not be null")
	private BirthDateModel birthDate;

	@JsonProperty("PersonName")
	@NotNull(message = "PersonName is required; it must not be null")
	private PersonNameModel personName;

	@JsonProperty("PersonContactInformation")
	@NotNull(message = "PersonContactInformation is required; it must not be null")
	private PersonContactInformationModel personContactInformation;

	public BirthDateModel getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(BirthDateModel birthDate) {
		this.birthDate = birthDate;
	}

	public PersonNameModel getPersonName() {
		return personName;
	}

	public void setPersonName(PersonNameModel personName) {
		this.personName = personName;
	}

	public PersonContactInformationModel getPersonContactInformation() {
		return personContactInformation;
	}

	public void setPersonContactInformation(PersonContactInformationModel personContactInformation) {
		this.personContactInformation = personContactInformation;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
