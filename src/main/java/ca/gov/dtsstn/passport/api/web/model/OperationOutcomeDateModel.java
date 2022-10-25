package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "OperationOutcomeDate")
public interface OperationOutcomeDateModel extends Serializable {

	@Default
	@Schema(required = true)
	@JsonProperty("DateTime")
	default Instant getDateTime() {
		return Instant.now();
	}

}
