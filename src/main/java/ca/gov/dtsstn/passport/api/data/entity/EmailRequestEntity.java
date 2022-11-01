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
@Entity(name = "EmailRequest")
@SuppressWarnings({ "serial" })
public class EmailRequestEntity extends AbstractEntity {

	public enum Status { EMAIL_NOT_FOUND, SENT }

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(length = 256, nullable = false)
	private String email;

	@Column(length = 32, nullable = false)
	private String fileNumber;

	@Column(length = 64, nullable = false)
	private String givenName;

	@Column(length = 64, nullable = false)
	private String surname;

	@Enumerated(EnumType.STRING)
	@Column(length = 32, nullable = false)
	private Status status;

	public EmailRequestEntity() {
		super();
	}

	@Builder.Constructor
	protected EmailRequestEntity( // NOSONAR (too many parameters)
		@Nullable String id,
		@Nullable String createdBy,
		@Nullable Instant createdDate,
		@Nullable String lastModifiedBy,
		@Nullable Instant lastModifiedDate,
		@Nullable Boolean isNew,
		@Nullable LocalDate dateOfBirth,
		@Nullable String email,
		@Nullable String fileNumber,
		@Nullable String givenName,
		@Nullable String surname,
		@Nullable Status status) {
	super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.fileNumber = fileNumber;
		this.givenName = givenName;
		this.surname = surname;
		this.status = status;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
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
			.append("dateOfBirth", dateOfBirth)
			.append("email", email)
			.append("fileNumber", fileNumber)
			.append("givenName", givenName)
			.append("surname", surname)
			.append("status", status)
			.toString();
	}

}
