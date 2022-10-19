package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "PassportStatusCreateRequest")
public class PassportStatusCreateRequestModel implements Serializable {

	public enum Status { APPROVED, IN_EXAMINATION, REJECTED }

	@NotBlank(message = "applicationRegisterSid is required; it must not be null or blank")
	@Schema(description = "An externally generated natural key that uniquely identifies a passport status in the system.", example = "ABCD1234", required = true)
	private String applicationRegisterSid;

	@DateTimeFormat(iso = ISO.DATE)
	@NotNull(message = "dateOfBirth is required; it must not be null")
	@PastOrPresent(message = "dateOfBirth must be a date in the past")
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
	private LocalDate dateOfBirth;

	@Email(message = "email must be a valid email address")
	@NotNull(message = "email is required; it must not be null")
	@Pattern(message = "email must be a valid email address", regexp = "[^@]+@[^@]+\\.[^@]+") // prevents user@localhost style emails
	@Schema(description = "The email address of the passport applicant.", example = "user@example.com", required = false)
	private String email;

	@NotBlank(message = "fileNumber is required; it must not be null or blank")
	@Schema(description = "The electronic service request file number.", example = "ABCD1234", required = true)
	private String fileNumber;

	@NotBlank(message = "firstName is required; it must not be null or blank")
	@Schema(description = "The first name of the passport applicant.", example = "John", required = true)
	private String firstName;

	@NotBlank(message = "lastName is required; it must not be null or blank")
	@Schema(description = "The last name of the passport applicant.", example = "Doe", required = true)
	private String lastName;

	@NotNull(message = "status is required; it must not be null")
	@Schema(description = "The status of the passport application.", required = true)
	private Status status;

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
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
