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
@Schema(name = "OperationOutcomeStatus")
@Style(validationMethod = ValidationMethod.NONE)
public interface OperationOutcomeStatusModel extends Serializable {

	@JsonProperty("StatusCode")
	String getStatusCode();

	@JsonProperty("StatusDescriptionText")
	String getStatusDescriptionText();

}
