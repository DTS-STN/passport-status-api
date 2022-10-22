package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PersonContactInformation")
@JsonDeserialize(as = ImmutablePersonContactInformationModel.class)
public interface PersonContactInformationModel extends Serializable {

	@Nullable
	@JsonProperty("ContactEmailID")
	@Email(message = "ContactEmailID must be a valid email address")
	@Pattern(message = "email must be a valid email address", regexp = "[^@]+@[^@]+\\.[^@]+")
	@Schema(description = "The email address of the certificate applicant.", example = "user@example.com")
	String getContactEmailId();

}
