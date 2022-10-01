package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing a passport status.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "PassportStatus")
@SuppressWarnings({ "serial" })
@Style(passAnnotations = { Relation.class })
@Relation(collectionRelation = "passportStatuses", itemRelation = "passportStatus")
public abstract class PassportStatusModel extends RepresentationModel<PassportStatusModel> implements Serializable {

	public enum Status {

		APPROVED,
		IN_EXAMINATION,
		REJECTED;

	}

	@Nullable
	@Schema(description = "The internal database ID of the passport status.", example = "476743eac593f65fd6964a15")
	public abstract String getId();

	@Nullable
	@Schema(description = "The creator of the resource.", example = "Passport Status API")
	public abstract String getCreatedBy();

	@Nullable
	@Schema(description = "The creation timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	public abstract Instant getCreatedDate();

	@Nullable
	@Schema(description = "The last modifier of the resource.", example = "Passport Status API")
	public abstract String getLastModifiedBy();

	@Nullable
	@Schema(description = "The last modification timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	public abstract Instant getLastModifiedDate();

	@Nullable
	@Schema(description = "The current version of the resource. Used to enforce opportunistic locking during updates.", example = "0")
	public abstract Long getVersion();

	@Nullable
	@Schema(description = "The electronic service request file number.", example = "ABCD1234")
	public abstract String getFileNumber();

	@Nullable
	@Schema(description = "The first name of the passport applicant.", example = "John")
	public abstract String getFirstName();

	@Nullable
	@Schema(description = "The last name of the passport applicant.", example = "Doe")
	public abstract String getLastName();

	@Nullable
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01")
	public abstract LocalDate getDateOfBirth();

	@Nullable
	@Schema(description = "The status of the passport application.")
	public abstract Status getStatus();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(@Nullable Object obj);

	@Override
	public abstract String toString();

}