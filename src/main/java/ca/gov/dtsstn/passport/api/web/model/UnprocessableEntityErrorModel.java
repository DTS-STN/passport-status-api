package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 422 Unprocessable Entity response.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
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
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
