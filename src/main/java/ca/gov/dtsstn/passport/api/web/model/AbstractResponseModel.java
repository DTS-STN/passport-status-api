package ca.gov.dtsstn.passport.api.web.model;

import java.time.Instant;
import java.util.Objects;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonView;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A base resource model containing fields common to all resources.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public class AbstractResponseModel<T extends AbstractResponseModel<? extends T>> extends RepresentationModel<T> {

	@Schema(description = "The internal unique ID of the resource.", example = "00000000-0000-0000-0000-000000000000")
	protected String id;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The creator of the resource.", example = "Passport Status API")
	protected String createdBy;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The creation timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	protected Instant createdDate;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The last modifier of the resource.", example = "Passport Status API")
	protected String lastModifiedBy;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The last modification timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	protected Instant lastModifiedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final AbstractResponseModel<?> other = (AbstractResponseModel<?>) obj;

		return Objects.equals(this.id, other.id)
			&& Objects.equals(this.createdBy, other.createdBy)
			&& Objects.equals(this.createdDate, other.createdDate)
			&& Objects.equals(this.lastModifiedBy, other.lastModifiedBy)
			&& Objects.equals(this.lastModifiedDate, other.lastModifiedDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
