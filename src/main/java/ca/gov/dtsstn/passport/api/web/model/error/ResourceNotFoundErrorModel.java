package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an HTTP 404 Not Found response.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "ResourceNotFoundError")
public interface ResourceNotFoundErrorModel extends Serializable {

	@Default
	@Schema(example = "404")
	default int getStatusCode() {
		return 404;
	}

	@Schema(example = "The resource with ID=[54543ab1-01b3-4edb-aad6-9b6c6b9e6985] was not found or the user does not have access")
	String getDetails();

	@Default
	@Schema(example = "API-0404")
	default String getErrorCode() {
		return "API-0404";
	}

	@Default
	@Schema(example = "Not found")
	default String getMessage() {
		return "Not found";
	}

	@Default
	@Schema(example = "2000-01-01T00:00:00Z")
	default Instant getTimestamp() {
		return Instant.now();
	}

}
