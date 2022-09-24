package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an API error.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "BadRequestError")
public interface BadRequestErrorModel extends Serializable {

	@Nullable
	@Schema(example = "[ \"'id' is required; it must not be null\" ]")
	List<String> getDetails();

	@Nullable
	@Schema(example = "API-0400")
	String getErrorCode();

	@Nullable
	@Schema(example = "Bad request")
	String getMessage();

	@Nullable
	@Schema(example = "2000-01-01T00:00:00Z")
	Instant getTimestamp();

	@Nullable
	List<ValidationErrorModel> getValidationErrors();

	@Immutable
	@Schema(name = "ValidationError")
	public interface ValidationErrorModel extends Serializable {

		@Nullable
		@Schema(example = "[ \"property.NotBlank\" ]")
		String getCode();

		@Nullable
		@Schema(example = "[ \"Id\" ]")
		String getField();

		@Nullable
		@Schema(example = "[ \"'id' is required; it must not be null\" ]")
		String getMessage();

	}

}
