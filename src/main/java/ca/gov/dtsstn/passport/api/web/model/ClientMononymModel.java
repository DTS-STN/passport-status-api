package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Immutable
@Schema(name = "ClientMononym")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableClientMononymModel.class)
public interface ClientMononymModel extends Serializable {

	@Valid
	@Default
	@JsonProperty("BirthDate")
	@NotNull(message = "BirthDate is required; it must not be null")
	default BirthDateModel getPersonBirthDate() {
		return ImmutableBirthDateModel.builder().build();
	}

	@Valid
	@Default
	@JsonProperty("PersonName")
	@NotNull(message = "PersonName is required; it must not be null")
	default PersonMononymModel getPersonName() {
		return ImmutablePersonMononymModel.builder().build();
	}

	@Valid
	@Default
	@JsonProperty("PersonContactInformation")
	@NotNull(message = "PersonContactInformation is required; it must not be null")
	default PersonContactInformationModel getPersonContactInformation() {
		return ImmutablePersonContactInformationModel.builder().build();
	}

	@Valid
	@Default
	@JsonProperty("PersonPreferredLanguage")
	@NotNull(message = "PersonPreferredLanguage is required; it must not be null")
	default PersonPreferredLanguageModel getPersonPreferredLanguage() {
		return ImmutablePersonPreferredLanguageModel.builder().build();
	}

}
