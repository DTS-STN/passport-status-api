package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.validation.PassportDeliveryMethodCode;
import ca.gov.dtsstn.passport.api.web.validation.PassportServiceLevelCode;
import ca.gov.dtsstn.passport.api.web.validation.PassportStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationTimelineDates")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableCertificateApplicationTimelineDatesModel.class)
public interface CertificateApplicationTimelineDatesModel extends Serializable {

  @Valid
	@Default
	@JsonProperty("ApplicationReceivedDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	@NotNull(message = "ApplicationReceivedDate is required; it must not be null")
	default ApplicationReceivedDateModel getApplicationReceivedDate() {
		return ImmutableApplicationReceivedDateModel.builder().build();
	}

  @Valid
	@Default
	@JsonProperty("ApplicationReviewedDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	default ApplicationReviewedDateModel getApplicationReviewedDate() {
		return ImmutableApplicationReviewedDateModel.builder().build();
	}

  @Valid
	@Default
	@JsonProperty("ApplicationPrintedDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	default ApplicationPrintedDateModel getApplicationPrintedDate() {
		return ImmutableApplicationPrintedDateModel.builder().build();
	}
  
  @Valid
	@Default
	@JsonProperty("ApplicationCompletedDate")
	@JsonView({ Authorities.AuthenticatedView.class })
	default ApplicationCompletedDateModel getApplicationCompletedDate() {
		return ImmutableApplicationCompletedDateModel.builder().build();
	}
}
