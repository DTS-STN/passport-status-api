package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 401 Unauthorized or an HTTP 403 Forbidden response
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "AuthenticationError")
public interface AuthenticationErrorModel extends Serializable {

	@Default
	@Schema(example = "401")
	default int getStatusCode() {
		return 401;
	}

	@Schema(example = "Unauthorized.")
	String getDetails();

	@Default
	@Schema(example = "API-0401")
	default String getErrorCode() {
		return "API-0401";
	}

	@Default
	@Schema(example = "Unauthorized")
	default String getMessage() {
		return "Unauthorized";
	}

	@Default
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
