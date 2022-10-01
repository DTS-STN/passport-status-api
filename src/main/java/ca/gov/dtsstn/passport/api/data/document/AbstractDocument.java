package ca.gov.dtsstn.passport.api.data.document;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.lang.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
public abstract class AbstractDocument implements Serializable {

	@Id
	protected String id;

	@CreatedBy
	protected String createdBy;

	@CreatedDate
	protected Instant createdDate;

	@LastModifiedBy
	protected String lastModifiedBy;

	@LastModifiedDate
	protected Instant lastModifiedDate;

	@Version
	protected Long version;

	protected AbstractDocument() {
		/* required by mongodb */
	}

	protected AbstractDocument(
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Long version) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.version = version;
	}

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final var other = (AbstractDocument) obj;

		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", id)
			.append("createdBy", createdBy)
			.append("createdDate", createdDate)
			.append("lastModifiedBy", lastModifiedBy)
			.append("lastModifiedDate", lastModifiedDate)
			.append("version", version)
			.toString();
	}

}
