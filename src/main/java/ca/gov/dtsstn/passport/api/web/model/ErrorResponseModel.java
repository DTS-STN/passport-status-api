package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "ErrorResponse")
@Style(validationMethod = ValidationMethod.NONE)
public interface ErrorResponseModel extends Serializable {

	@Schema(required = false)
	@JsonProperty("CorrelationID")
	String getCorrelationId();

	@Schema(required = true)
	@JsonProperty("OperationOutcome")
	OperationOutcomeModel getOperationOutcome();

}
