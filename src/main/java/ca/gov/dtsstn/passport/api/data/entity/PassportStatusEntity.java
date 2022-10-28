package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

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
	private String firstName;

	@Column(length = 64, nullable = false)
	private String lastName;

	@ManyToOne()
    @JoinColumn(name = "status_code_id")
    private StatusCodeEntity statusCode;

	@Column(nullable = false)
	private LocalDate statusDate;

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
			@Nullable StatusCodeEntity statusCode,
			@Nullable LocalDate statusDate) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.applicationRegisterSid = applicationRegisterSid;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.fileNumber = fileNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.statusCode = statusCode;
		this.statusDate = statusDate;
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
			.append("statusCode", statusCode)
			.append("statusDate", statusDate)
			.toString();
	}

}
