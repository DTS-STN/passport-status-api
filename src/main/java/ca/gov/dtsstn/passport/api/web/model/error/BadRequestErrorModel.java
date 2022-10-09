package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 400 Bad Request response.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "BadRequestError")
public interface BadRequestErrorModel extends Serializable {

	@Default
	@Schema(example = "400")
	default int getStatusCode() {
		return 400;
	}

	@Nullable
	@ArraySchema(schema = @Schema(example = "firstName must not be null or blank"))
	List<String> getDetails();

	@Default
	@Schema(example = "API-0400")
	default String getErrorCode() {
		return "API-0400";
	}

	@Default
	@Schema(example = "Bad request")
	default String getMessage() {
		return "Bad request";
	}

	@Default
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

	@Nullable
	List<FieldValidationErrorModel> getFieldValidationErrors();

	@Immutable
	@Schema(name = "FieldValidationError")
	public interface FieldValidationErrorModel extends Serializable {

		@Schema(example = "NotBlank")
		String getCode();

		@Schema(example = "firstName")
		String getField();

		@Schema(example = "firstName must not be null or blank")
		String getMessage();

	}

}
