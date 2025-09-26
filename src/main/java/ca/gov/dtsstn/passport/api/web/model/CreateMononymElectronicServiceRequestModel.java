package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Immutable
@Schema(name = "CreateMononymElectronicServiceRequest")
@JsonDeserialize(as = ImmutableCreateMononymElectronicServiceRequestModel.class)
public interface CreateMononymElectronicServiceRequestModel extends Serializable {

	@Valid
	@Default
	@JsonProperty("Client")
	@NotNull(message = "Client is required; it must not be null")
	default ClientMononymModel getClient() {
		return ImmutableClientMononymModel.builder().build();
	}

}
