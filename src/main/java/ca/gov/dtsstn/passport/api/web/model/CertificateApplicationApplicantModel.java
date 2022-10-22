package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
@Schema(name = "CertificateApplicationApplicant")
@JsonDeserialize(as = ImmutableCertificateApplicationApplicantModel.class)
public interface CertificateApplicationApplicantModel extends Serializable {

	@JsonProperty("BirthDate")
	@NotNull(message = "BirthDate is required; it must not be null")
	BirthDateModel getBirthDate();

	@JsonProperty("PersonName")
	@NotNull(message = "PersonName is required; it must not be null")
	PersonNameModel getPersonName();

	@JsonProperty("PersonContactInformation")
	@NotNull(message = "PersonContactInformation is required; it must not be null")
	PersonContactInformationModel getPersonContactInformation();

}
