package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PersonName")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutablePersonNameModel.class)
public interface PersonNameModel extends Serializable {

	@JsonProperty("PersonGivenName")
	@Nullable
	@JsonSetter(nulls = Nulls.SKIP)
	@Size(min = 1, max = 1, message = "PersonGivenName must be an array of size 1")
	@Schema(description = "A set of given names of the certificate applicant. May be null in case of a mononym, but if present must contain exactly one non-blank entry.", example = "[\"John\"]")
	List<@NotBlank(message = "PersonGivenName entries must not be blank") @Size(max = 128, message = "PersonGivenName must be 128 characters or less") String> getPersonGivenNames();

	@JsonProperty("PersonSurName")
	@Size(max = 128, message = "PersonSurName must be 128 characters or less")
	@NotBlank(message = "PersonSurName is required; it must not be null or blank")
	@Schema(description = "The surname of the certificate applicant.", example = "Doe")
	String getPersonSurname();

}
