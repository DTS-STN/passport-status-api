package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an API validation error.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "ApiValidationError")
public interface ApiValidationErrorModel extends Serializable {

	@Schema(example = "[ \"property.NotBlank\" ]")
	@JsonProperty(access = Access.READ_ONLY)
	@Nullable String getCode();

	@Schema(example = "[ \"Id\" ]")
	@JsonProperty(access = Access.READ_ONLY)
	@Nullable String getField();

	@Schema(example = "[ \"'id' is required; it must not be null\" ]")
	@JsonProperty(access = Access.READ_ONLY)
	@Nullable String getMessage();

}
