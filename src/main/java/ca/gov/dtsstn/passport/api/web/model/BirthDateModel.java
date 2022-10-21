package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "BirthDate")
public class BirthDateModel implements Serializable {

	@JsonProperty("Date")
	@PastOrPresent(message = "Date must be in the past")
	@NotNull(message = "Date is required; it must not be null")
	@Schema(description = "The birth date of the certificate applicant in ISO 8601 format.", example = "2000-01-01")
	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
