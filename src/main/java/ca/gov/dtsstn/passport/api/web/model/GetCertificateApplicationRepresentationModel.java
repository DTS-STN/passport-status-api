package ca.gov.dtsstn.passport.api.web.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "GetCertificateApplicationResponseModel")
@JsonPropertyOrder({ "id", "CertificateApplication", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" })
@Relation(collectionRelation = "GetCertificateApplicationResponse", itemRelation = "GetCertificateApplicationResponse")
public class GetCertificateApplicationRepresentationModel extends AbstractRepresentationModel<GetCertificateApplicationRepresentationModel> {

	@JsonProperty("CertificateApplication")
	@NotNull(message = "CertificateApplication is required; it must not be null")
	private CertificateApplicationModel certificateApplication;

	@Nullable
	public CertificateApplicationModel getCertificateApplication() {
		return certificateApplication;
	}

	public void setCertificateApplication(@Nullable CertificateApplicationModel certificateApplication) {
		this.certificateApplication = certificateApplication;
	}

	@Override // Required to fix a weird vscode/eclipse & mapstruct bug (unmapped target property: "add") 💩
	public GetCertificateApplicationRepresentationModel add(Link link) { // NOSONAR (do-nothing inherited method)
		return super.add(link);
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) { return true; }
		if (obj == null || !super.equals(obj)) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final GetCertificateApplicationRepresentationModel other = (GetCertificateApplicationRepresentationModel) obj;

		return Objects.equals(certificateApplication, other.certificateApplication);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), certificateApplication);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
