package ca.gov.dtsstn.passport.api.web.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Schema(name = "PassportStatus")
@Relation(collectionRelation = "passportStatuses", itemRelation = "passportStatus")
public class PassportStatusReadResponseModel extends AbstractResponseModel<PassportStatusReadResponseModel> {

	public enum Status { APPROVED, IN_EXAMINATION, REJECTED }

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "An externally generated natural key that uniquely identifies a passport status in the system.", example = "ABCD1234")
	private String applicationRegisterSid;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01")
	private LocalDate dateOfBirth;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The email address of the passport applicant.", example = "user@example.com")
	private String email;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The electronic service request file number.", example = "ABCD1234")
	private String fileNumber;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The first name of the passport applicant.", example = "John")
	private String firstName;

	@JsonView({ Authorities.AuthenticatedView.class })
	@Schema(description = "The last name of the passport applicant.", example = "Doe")
	private String lastName;

	@Schema(description = "The status of the passport application.")
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

	@Override // Required to fix a weird vscode/eclipse & mapstruct bug (unmapped target property: "add") 💩
	public PassportStatusReadResponseModel add(Link link) { // NOSONAR (do-nothing inherited method)
		return super.add(link);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final PassportStatusReadResponseModel other = (PassportStatusReadResponseModel) obj;

		return Objects.equals(applicationRegisterSid, other.applicationRegisterSid)
			&& Objects.equals(dateOfBirth, other.dateOfBirth)
			&& Objects.equals(email, other.email)
			&& Objects.equals(fileNumber, other.fileNumber)
			&& Objects.equals(firstName, other.firstName)
			&& Objects.equals(lastName, other.lastName)
			&& Objects.equals(status, other.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), applicationRegisterSid, dateOfBirth, email, fileNumber, firstName, lastName, status);
	}

}
