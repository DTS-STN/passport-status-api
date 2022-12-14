package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "OperationOutcomeStatus")
public interface OperationOutcomeStatusModel extends Serializable {

	@Schema(required = true)
	@JsonProperty("StatusCode")
	String getStatusCode();

	@Schema(required = true)
	@JsonProperty("StatusDescriptionText")
	String getStatusDescriptionText();

}
