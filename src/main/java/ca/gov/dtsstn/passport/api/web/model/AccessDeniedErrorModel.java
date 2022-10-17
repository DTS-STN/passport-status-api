package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 403 Forbidden response
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "AccessDeniedError")
public interface AccessDeniedErrorModel extends Serializable {

	@Default
	@Schema(example = "403")
	default int getStatusCode() {
		return 403;
	}

	@Parameter
	@Schema(example = "Forbidden.")
	String getDetails();

	@Default
	@Schema(example = "API-0403")
	default String getErrorCode() {
		return "API-0403";
	}

	@Default
	@Schema(example = "Forbidden")
	default String getMessage() {
		return "Forbidden";
	}

	@Default
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
