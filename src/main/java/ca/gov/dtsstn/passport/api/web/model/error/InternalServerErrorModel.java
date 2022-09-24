package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing an API error.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "InternalServerError")
public interface InternalServerErrorModel extends Serializable {

	@Nullable
	@Schema(example = "An unexpected error has occurred.")
	String getDetails();

	@Nullable
	@Schema(example = "API-0500")
	String getErrorCode();

	@Nullable
	@Schema(example = "We are currently experiencing technical difficulties.")
	String getMessage();

	@Schema(example = "2000-01-01T00:00:00Z")
	@Nullable Instant getTimestamp();

}
