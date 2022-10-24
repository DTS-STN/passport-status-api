package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.Valid;
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
@Schema(name = "CertificateApplicationApplicant")
@JsonDeserialize(as = ImmutableCertificateApplicationApplicantModel.class)
public interface CertificateApplicationApplicantModel extends Serializable {

	@Valid
	@Nullable
	@JsonProperty("BirthDate")
	@NotNull(message = "BirthDate is required; it must not be null")
	BirthDateModel getBirthDate();

	@Valid
	@Nullable
	@JsonProperty("PersonName")
	@NotNull(message = "PersonName is required; it must not be null")
	PersonNameModel getPersonName();

	@Valid
	@Nullable
	@JsonProperty("PersonContactInformation")
	@NotNull(message = "PersonContactInformation is required; it must not be null")
	PersonContactInformationModel getPersonContactInformation();

}
