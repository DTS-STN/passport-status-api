package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.validation.ReferenceDataId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "SourceCode")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableSourceCodeModel.class)
public interface SourceCodeModel extends Serializable {

	@JsonProperty("ReferenceDataID")
	@ReferenceDataId(message = "ReferenceDataID is invalid or unknown")
	@NotBlank(message = "ReferenceDataID is required; it must not be null or blank")
	@Schema(description = "The source ID of the certificate application data.", example = "33")
	String getReferenceDataId();

}
