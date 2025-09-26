package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Immutable
@Schema(name = "PersonMononym")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutablePersonMononymModel.class)
public interface PersonMononymModel extends Serializable {

	@JsonProperty("PersonMononym")
	@Size(max = 128, message = "PersonMononym must be 128 characters or less")
	@NotBlank(message = "PersonMononym is required; it must not be null or blank")
	@Schema(description = "The mononym of the certificate applicant.", example = "Spock")
	String getPersonMononym();

}
