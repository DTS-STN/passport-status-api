package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
@Entity(name = "PassportStatus")
public class PassportStatusEntity extends AbstractEntity {

	public enum Status { APPROVED, IN_EXAMINATION, REJECTED }

	@Column(length = 256, nullable = false)
	private String applicationRegisterSid;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(length = 256, nullable = false)
	private String email;

	@Column(length = 32, nullable = false)
	private String fileNumber;

	@Column(length = 64, nullable = false)
	private String firstName;

	@Column(length = 64, nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(length = 32, nullable = false)
	private Status status;

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
			@Nullable String firstName,
			@Nullable String lastName,
			@Nullable Status status) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.applicationRegisterSid = applicationRegisterSid;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.fileNumber = fileNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("status", status)
			.toString();
	}

}
