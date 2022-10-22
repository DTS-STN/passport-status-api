package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationDate")
@JsonDeserialize(as = ImmutableCertificateApplicationDateModel.class)
public interface CertificateApplicationDateModel extends Serializable {

	@Nullable
	@JsonProperty("Date")
	@NotNull(message = "Date is required; it must not be null")
	@Schema(description = "The date the certificate application was created in ISO 8601 format.", example = "2020-01-01")
	LocalDate getDate();

}
