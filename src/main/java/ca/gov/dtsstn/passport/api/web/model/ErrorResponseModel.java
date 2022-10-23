package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
public interface ErrorResponseModel extends Serializable {

	@JsonProperty("CorrelationID")
	String getCorrelationId();

	@JsonProperty("OperationOutcome")
	OperationOutcomeModel getOperationOutcome();

}
