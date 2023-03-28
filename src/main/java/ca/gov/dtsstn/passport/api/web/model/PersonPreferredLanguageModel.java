package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PersonPreferredLanguage")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutablePersonPreferredLanguageModel.class)
public interface PersonPreferredLanguageModel extends Serializable {

	public enum LanguageName { ENGLISH, FRENCH }

	@JsonProperty("LanguageName")
	@NotNull(message = "LanguageName is required; it must not be null")
	@ValueOfEnum(enumClass = LanguageName.class, message = "LanguageName is invalid or unknown")
	@Schema(description = "The preferred language name of the certificate applicant.", example = "ENGLISH")
	String getLanguageName();

}
