package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "ResourceMeta")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableResourceMetaModel.class)
public interface ResourceMetaModel extends Serializable {

	@JsonProperty("VersionID")
	@NotBlank(message = "VersionID is required; it must not be null or blank")
	@Digits(message = "VersionID must be a numeric string", integer = 16, fraction = 0)
	@Schema(description = "The version of the resource content. Can be used to ensure that updates are based on the latest version of the resource.", example = "0000")
	String getVersion();

}
