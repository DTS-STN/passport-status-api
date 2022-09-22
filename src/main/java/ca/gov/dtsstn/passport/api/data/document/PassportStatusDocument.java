package ca.gov.dtsstn.passport.api.data.document;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

/**
 * MongoDB document representing a passport status.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@TypeAlias("PassportStatus")
@Document("passportStatuses")
public interface PassportStatusDocument extends Serializable {

	public enum Status {

		// TODO :: GjB :: this are obviously made up
		ACCEPTED, PROCESSING, REJECTED, COMPLETED;

	}

	@Id
	@Nullable
	String getId();

	@Nullable
	@CreatedBy
	String getCreatedBy();

	@Nullable
	@CreatedDate
	Instant getCreatedDate();

	@Nullable
	@LastModifiedBy
	String getLastModifiedBy();

	@Nullable
	@LastModifiedDate
	Instant getLastModifiedDate();

	@Version
	@Nullable
	Long getVersion();

	@Nullable
	String getFileNumber();

	@Nullable
	String getFirstName();

	@Nullable
	String getLastName();

	@Nullable
	LocalDate getDateOfBirth();

	@Nullable
	Status getStatus();

}
