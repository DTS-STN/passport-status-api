package ca.gov.dtsstn.passport.api.data.document;

import java.time.Instant;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;


/**
 * MongoDB document representing a passport status.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@TypeAlias("PassportStatus")
@Document("passport-statuses")
@SuppressWarnings({ "serial" })
public class PassportStatusDocument extends AbstractDocument {

	@Indexed
	private String dateOfBirth;

	@Indexed
	private String fileNumber;

	@Indexed
	private String firstName;

	@Indexed
	private String lastName;

	private Status status;

	public PassportStatusDocument() {
		super();
	}

	@Builder.Constructor
	protected PassportStatusDocument( // NOSONAR
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Long version,
			@Nullable String fileNumber,
			@Nullable String firstName,
			@Nullable String lastName,
			@Nullable String dateOfBirth,
			@Nullable Status status) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, version);
		this.fileNumber = fileNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.status = status;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
		return new ToStringCreator(this)
			.append("super", super.toString())
			.append("dateOfBirth", dateOfBirth)
			.append("fileNumber", fileNumber)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("status", status)
			.toString();
	}

	public enum Status {

		APPROVED,
		IN_EXAMINATION,
		REJECTED;

	}

}
