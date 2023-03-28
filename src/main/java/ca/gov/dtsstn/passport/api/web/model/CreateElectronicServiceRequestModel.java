package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CreateElectronicServiceRequest")
@JsonDeserialize(as = ImmutableCreateElectronicServiceRequestModel.class)
public interface CreateElectronicServiceRequestModel extends Serializable {

	@Valid
	@Default
	@JsonProperty("Client")
	@NotNull(message = "Client is required; it must not be null")
	default ClientModel getClient() {
		return ImmutableClientModel.builder().build();
	}

}
