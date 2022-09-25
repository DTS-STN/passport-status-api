package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 422 Unprocessable Entity response.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "UnprocessableEntityError")
public interface UnprocessableEntityErrorModel extends Serializable {

	@Default
	@Schema(example = "422")
	default int getStatusCode() {
		return 422;
	}

	@Schema(example = "Search query returned non-unique results")
	String getDetails();

	@Default
	@Schema(example = "API-0422")
	default String getErrorCode() {
		return "API-0422";
	}

	@Default
	@Schema(example = "Unprocessable entity")
	default String getMessage() {
		return "Unprocessable entity";
	}

	@Default
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
