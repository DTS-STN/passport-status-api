package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "PersonName")
public class PersonNameModel implements Serializable {

	@JsonProperty("PersonGivenName")
	@NotNull(message = "PersonGivenName is required; it must not be null")
	@Size(min = 1, max = 1, message = "PersonGivenName must be an array of size 1")
	@Schema(description = "A set of given names of the certificate applicant.", example = "[\"John\"]")
	private List<String> personGivenNames;

	@JsonProperty("PersonSurName")
	@NotBlank(message = "PersonSurName is required; it must not be null or blank")
	@Schema(description = "The surname of the certificate applicant.", example = "Doe")
	private String personSurname;

	public List<String> getPersonGivenNames() {
		return personGivenNames;
	}

	public void setPersonGivenNames(List<String> personGivenNames) {
		this.personGivenNames = personGivenNames;
	}

	public String getPersonSurname() {
		return personSurname;
	}

	public void setPersonSurname(String personSurname) {
		this.personSurname = personSurname;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
