package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "Error")
public interface ErrorModel extends Serializable {

	@JsonProperty(access = Access.READ_ONLY)
	@Schema(example = "[ \"'id' is required; it must not be null\" ]")
	@Nullable List<String> getDetails();

	@Schema(example = "API-0000")
	@JsonProperty(access = Access.READ_ONLY)
	@Nullable String getErrorCode();

	@JsonProperty(access = Access.READ_ONLY)
	@Schema(example = "We are currently experiencing technical difficulties.")
	@Nullable String getMessage();

	@JsonProperty(access = Access.READ_ONLY)
	@Schema(example = "2000-01-01T00:00:00Z")
	@Nullable Instant getTimestamp();

}
