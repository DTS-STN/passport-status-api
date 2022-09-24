package ca.gov.dtsstn.passport.api.web.model;

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
@Schema(name = "ResourceNotFoundError")
public interface ResourceNotFoundErrorModel extends Serializable {

	@Nullable
	@Schema(example = "API-0404")
	String getErrorCode();

	@Nullable
	@Schema(example = "The resource with ID=[54543ab1-01b3-4edb-aad6-9b6c6b9e6985] was not found or the user does not have access.")
	String getMessage();

	@Nullable
	@Schema(example = "2000-01-01T00:00:00Z")
	Instant getTimestamp();

}
