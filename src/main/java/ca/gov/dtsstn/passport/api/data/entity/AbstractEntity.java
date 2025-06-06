package ca.gov.dtsstn.passport.api.data.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.data.annotation.EntityId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

/**
 *
 * Base class for all {@link Entity} classes. Provides tombstone columns that are common to all database tables:
 *
 * <li>{@code id}
 * <li>{@code createdBy}
 * <li>{@code createdDate}
 * <li>{@code lastModifiedBy}
 * <li>{@code lastModifiedDate}
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@MappedSuperclass
@SuppressWarnings({ "serial" })
@EntityListeners({ AuditingEntityListener.class })
public abstract class AbstractEntity implements Persistable<String>, Serializable {

	@Id
	@EntityId
	@Column(length = 64, nullable = false, updatable = false)
	protected String id;

	@CreatedBy
	@Column(length = 64, nullable = false, updatable = false)
	protected String createdBy;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected Instant createdDate;

	@LastModifiedBy
	@Column(length = 64, nullable = true)
	protected String lastModifiedBy;

	@LastModifiedDate
	@Column(nullable = true)
	protected Instant lastModifiedDate;

	@Transient(/* to be used by Spring Data to persist vs merge */)
	protected Boolean isNew;

	protected AbstractEntity() {
		/* required by JPA */
	}

	protected AbstractEntity(
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Boolean isNew) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isNew = isNew;
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

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public boolean isNew() {
		return Optional.ofNullable(isNew).orElse(id == null);
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final var other = (AbstractEntity) obj;

		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("id", id)
			.append("isNew", isNew())
			.append("createdBy", createdBy)
			.append("createdDate", createdDate)
			.append("lastModifiedBy", lastModifiedBy)
			.append("lastModifiedDate", lastModifiedDate)
			.toString();
	}

}
