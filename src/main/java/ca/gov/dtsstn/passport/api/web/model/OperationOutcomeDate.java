package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
public interface OperationOutcomeDate extends Serializable {

	@Default
	@JsonProperty("DateTime")
	default Instant getDateTime() {
		return Instant.now();
	}

}
