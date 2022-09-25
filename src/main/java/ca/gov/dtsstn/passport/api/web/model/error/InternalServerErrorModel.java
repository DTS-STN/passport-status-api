package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 500 Internal Server Error response
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "InternalServerError")
public interface InternalServerErrorModel extends Serializable {

	@Default
	@Schema(example = "500")
	default int getStatusCode() {
		return 500;
	}

	@Schema(example = "00000000-0000-0000-000000000000")
	String getCorrelationId();

	@Schema(example = "An unexpected error has occurred.")
	String getDetails();

	@Default
	@Schema(example = "API-0500")
	default String getErrorCode() {
		return "API-0500";
	}

	@Default
	@Schema(example = "Internal server error")
	default String getMessage() {
		return "Internal server error";
	}

	@Default
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
