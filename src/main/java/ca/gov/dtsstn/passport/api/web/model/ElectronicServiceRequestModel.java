package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@JsonDeserialize(as = ImmutableElectronicServiceRequestModel.class)
public interface ElectronicServiceRequestModel extends Serializable {

	@Email(message = "email must be a valid email address")
	@Pattern(message = "email must be a valid email address", regexp = "[^@]+@[^@]+\\.[^@]+") // prevents user@localhost style emails
	@Schema(description = "The email address of the user submitting the electronic service request.", required = true, example = "user@example.com")
	String getEmail();

}
