package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
@Schema(name = "CertificateApplicationIdentification")
@JsonDeserialize(as = ImmutableCertificateApplicationIdentificationModel.class)
public interface CertificateApplicationIdentificationModel extends Serializable {

	static final String APPLICATION_REGISTER_SID_CATEGORY_TEXT = "Application Register SID";

	static final String FILE_NUMBER_CATEGORY_TEXT = "File Number";

	static final String MANIFEST_NUMBER_CATEGORY_TEXT = "Manifest Number";

	@JsonProperty("IdentificationCategoryText")
	@NotNull(message = "IdentificationCategoryText is required; it must not be null")
	@Schema(description = "A human readable description of the certificate application ID entry.", example = "Application Register SID", allowableValues = { "Application Register SID", "File Number", "Manifest Number" })
	String getIdentificationCategoryText();

	@JsonProperty("IdentificationID")
	@Size(max = 50, message = "IdentificationID must be 50 characters or less")
	@Schema(description = "The value of the certificate application ID entry.", example = "ABCD1234")
	String getIdentificationId();

}
