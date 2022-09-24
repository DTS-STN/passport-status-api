package ca.gov.dtsstn.passport.api.web.model.error;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "NonUniqueResourceError")
public interface NonUniqueResourceErrorModel extends Serializable {

	@Nullable
	@Schema(example = "API-0422")
	String getErrorCode();

	@Nullable
	@Schema(example = "The search query returned a non-unique result.")
	String getMessage();

	@Nullable
	@Schema(example = "2000-01-01T00:00:00Z")
	Instant getTimestamp();

}
