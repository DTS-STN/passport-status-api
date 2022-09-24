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
@Schema(name = "UnprocessableEntityError")
public interface UnprocessableEntityErrorModel extends Serializable {

	@Nullable
	@Schema(example = "Search query returned non-unique results")
	String getDetails();

	@Nullable
	@Schema(example = "API-0422")
	String getErrorCode();

	@Nullable
	@Schema(example = "Unprocessable entity")
	String getMessage();

	@Nullable
	@Schema(example = "2000-01-01T00:00:00Z")
	Instant getTimestamp();

}
