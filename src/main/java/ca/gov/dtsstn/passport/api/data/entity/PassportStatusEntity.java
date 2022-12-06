package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
@Entity(name = "PassportStatus")
public class PassportStatusEntity extends AbstractEntity {

	@Column(length = 256, nullable = false)
	private String applicationRegisterSid;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(length = 256, nullable = false)
	private String email;

	@Column(length = 32, nullable = false)
	private String fileNumber;

	@Column(length = 64, nullable = false)
	private String givenName;

	@Column(length = 32, nullable = true)
	private String manifestNumber;

	@Column(length = 64, nullable = false)
	private String surname;

	@ManyToOne()
	private StatusCodeEntity statusCode;

	@Column(nullable = false)
	private LocalDate statusDate;

	@Column(nullable = false)
	private Long version;

	public PassportStatusEntity() {
		super();
	}

	@Builder.Constructor
	protected PassportStatusEntity( // NOSONAR (too many parameters)
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Boolean isNew,
			@Nullable String applicationRegisterSid,
			@Nullable LocalDate dateOfBirth,
			@Nullable String email,
			@Nullable String fileNumber,
			@Nullable String givenName,
			@Nullable String manifestNumber,
			@Nullable String surname,
			@Nullable StatusCodeEntity statusCode,
			@Nullable LocalDate statusDate,
			@Nullable Long version) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.applicationRegisterSid = applicationRegisterSid;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.fileNumber = fileNumber;
		this.givenName = givenName;
		this.manifestNumber = manifestNumber;
		this.surname = surname;
		this.statusCode = statusCode;
		this.statusDate = statusDate;
		this.version = version;
	}

	public String getApplicationRegisterSid() {
		return applicationRegisterSid;
	}

	public void setApplicationRegisterSid(String applicationRegisterSid) {
		this.applicationRegisterSid = applicationRegisterSid;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getManifestNumber() {
		return manifestNumber;
	}

	public void setManifestNumber(String manifestNumber) {
		this.manifestNumber = manifestNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public StatusCodeEntity getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(StatusCodeEntity statusCode) {
		this.statusCode = statusCode;
	}

	public LocalDate getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(LocalDate statusDate) {
		this.statusDate = statusDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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
			.append("applicationRegisterSid", applicationRegisterSid)
			.append("dateOfBirth", dateOfBirth)
			.append("email", email)
			.append("fileNumber", fileNumber)
			.append("givenName", givenName)
			.append("manifestNumber", manifestNumber)
			.append("surname", surname)
			.append("statusCode", Optional.ofNullable(statusCode)
				.map(StatusCodeEntity::getCode)
				.orElse(null))
			.append("statusDate", statusDate)
			.append("version", version)
			.toString();
	}

}
