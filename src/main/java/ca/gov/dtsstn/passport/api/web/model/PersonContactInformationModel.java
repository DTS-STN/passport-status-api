package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PersonContactInformation")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutablePersonContactInformationModel.class)
public interface PersonContactInformationModel extends Serializable {

	@JsonProperty("ContactEmailID")
	@Size(max = 256, message = "ContactEmailID must be 256 characters or less")
	@Schema(description = "The email address of the certificate applicant.", example = "user@example.com")
	@Email(message = "ContactEmailID must be a valid email address", regexp = "(^$)|(^[^@]+@[^@]+\\.[^@]+$)")
	String getContactEmailId();

}
