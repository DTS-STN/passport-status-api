package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "DeliveryMethodCode")
@SuppressWarnings({ "serial" })
public class DeliveryMethodCodeEntity extends AbstractEntity {

	@Column(length = 16, nullable = false)
	private String code;

	@Column(length = 8, nullable = false)
	private String cdoCode;

	@Column(length = 256)
	private String description;

	@Column(nullable = false)
	private Boolean isActive;

	public DeliveryMethodCodeEntity() {
		super();
	}

	@Builder.Constructor
	protected DeliveryMethodCodeEntity( // NOSONAR (too many parameters)
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Boolean isNew,
			@Nullable String code,
			@Nullable String cdoCode,
			@Nullable String description,
			@Nullable Boolean isActive) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.code = code;
		this.cdoCode = cdoCode;
		this.description = description;
		this.isActive = isActive;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCdoCode() {
		return this.cdoCode;
	}

	public void setCdoCode(String cdoCode) {
		this.cdoCode = cdoCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isIsActive() {
		return this.isActive;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean equals(Object obj) {
		// keeps SonarLint happy
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// keeps SonarLint happy
		return super.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("super", super.toString())
			.append("code", code)
			.append("cdoCode", cdoCode)
			.append("description", description)
			.append("isActive", isActive)
			.toString();
	}

}
