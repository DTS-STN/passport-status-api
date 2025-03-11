package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Immutable
@Schema(name = "CertificateApplicationTimelineDate")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationTimelineDateModel.class)
public interface CertificateApplicationTimelineDateModel extends Serializable {

  // Current potential ReferenceDataNames:
  // - Received
  // - Reviewed
  // - Printed
  // - Completed
  static final String RECEIVED_REFERENCE_DATA_TEXT = "Received";
  static final String REVIEWED_REFERENCE_DATA_TEXT = "Reviewed";
  static final String PRINTED_REFERENCE_DATA_TEXT = "Printed";
  static final String COMPLETED_REFERENCE_DATA_TEXT = "Completed";

	@NotBlank(message = "ReferenceDataName cannot be blank")
	@NotNull(message = "ReferenceDataName is required; it must not be null")
  @Schema(description = "The identifier of which timeline date we're dealing with", example = "Received")
  @JsonProperty("ReferenceDataName")
  String getReferenceDataName();
	

  @Valid
  @Default
  @JsonProperty("TimelineDate")
  @NotNull(message = "Timeline date is required; it must not be null")
  default TimelineDateModel getTimelineDate() {
    return ImmutableTimelineDateModel.builder().build();
  }
}
