package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PersonName")
@JsonDeserialize(as = ImmutablePersonNameModel.class)
public interface PersonNameModel extends Serializable {

	@Nullable
	@JsonProperty("PersonGivenName")
	@NotNull(message = "PersonGivenName is required; it must not be null")
	@Size(min = 1, max = 1, message = "PersonGivenName must be an array of size 1")
	@Schema(description = "A set of given names of the certificate applicant.", example = "[\"John\"]")
	List<String> getPersonGivenNames();

	@Nullable
	@JsonProperty("PersonSurName")
	@NotBlank(message = "PersonSurName is required; it must not be null or blank")
	@Schema(description = "The surname of the certificate applicant.", example = "Doe")
	String getPersonSurname();

}
