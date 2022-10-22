package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "CertificateApplicationStatus")
@JsonDeserialize(as = ImmutableCertificateApplicationStatusModel.class)
public interface CertificateApplicationStatusModel extends Serializable {


	@Nullable
	@JsonProperty("StatusCode")
	@NotBlank(message = "StatusCode is required; it must not null or blank")
	@Schema(description = "The certificate application status code.", example = "000")
	String getStatusCode();

}
