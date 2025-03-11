package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Immutable
@Schema(name = "TimelineDate")
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableTimelineDateModel.class)
public interface TimelineDateModel extends Serializable {

	@JsonProperty("Date")
	@Size(min = 10, max = 10, message = "Date must be exactly 10")
	@NotBlank(message = "Date is required; it must not be null or blank")
	@PastOrPresent(message = "Date must be a valid ISO 8601 date format (yyyy-mm-dd) and in the past")
	@Schema(description = "The timeline date in ISO 8601 format.", example = "2000-01-01", implementation = LocalDate.class)
	String getDate();

}
