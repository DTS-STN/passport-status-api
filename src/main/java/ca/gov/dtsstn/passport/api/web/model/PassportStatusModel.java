package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST model representing a passport status.
 * <p>
 * <b>Important:</b> this class is not actually immutable!
 * The {@code @Immutable} annotation is used simply as a code generation mechanism to avoid having to write a dozen getters and setters.
 *
 * 	TODO :: GjB :: consider removing the immutable annotation and to create a mutable class
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@SuppressWarnings({ "serial" })
@Schema(name = "PassportStatus")
@Style(passAnnotations = { Relation.class })
@JsonDeserialize(as = ImmutablePassportStatusModel.class)
@Relation(collectionRelation = "passportStatuses", itemRelation = "passportStatus")
@JsonPropertyOrder(value = { "id", "applicationRegisterSid", "dateOfBirth", "email", "fileNumber", "firstName", "lastName", "status", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "version" })
public abstract class PassportStatusModel extends RepresentationModel<PassportStatusModel> implements Serializable {

	public interface Views {
		public interface POST {}
		public interface GET {}
		public interface PUT {}

	}

	public enum Status {

		APPROVED,
		IN_EXAMINATION,
		REJECTED;

	}

	@Nullable
	@JsonView({ Views.GET.class })
	@Schema(description = "The internal database ID of the passport status.", example = "476743eac593f65fd6964a15")
	public abstract String getId();

	@Nullable
	@JsonView({ Views.GET.class })
	@Schema(description = "The creator of the resource.", example = "Passport Status API")
	public abstract String getCreatedBy();

	@Nullable
	@JsonView({ Views.GET.class })
	@Schema(description = "The creation timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	public abstract Instant getCreatedDate();

	@Nullable
	@JsonView({ Views.GET.class })
	@Schema(description = "The last modifier of the resource.", example = "Passport Status API")
	public abstract String getLastModifiedBy();

	@Nullable
	@JsonView({ Views.GET.class })
	@Schema(description = "The last modification timestamp of the resource in ISO-8601 format.", example = "2000-01-01T00:00:00.000Z")
	public abstract Instant getLastModifiedDate();

	@Nullable
	@JsonView({ Views.GET.class, Views.PUT.class })
	@Schema(description = "The current version of the resource. Used to enforce opportunistic locking during updates.", example = "0")
	public abstract Long getVersion();

	@Nullable
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@NotBlank(message = "applicationRegisterSid is required; it must not be null or blank")
	@Schema(description = "An externally generated natural key that uniquely identifies a passport status in the system.", example = "ABCD1234", required = true)
	public abstract String getApplicationRegisterSid();

	@Nullable
	@DateTimeFormat(iso = ISO.DATE)
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@NotNull(message = "dateOfBirth is required; it must not be null")
	@PastOrPresent(message = "dateOfBirth must be a date in the past")
	@Schema(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
	public abstract LocalDate getDateOfBirth();

	@Nullable
	@Email(message = "email must be a valid email address")
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@Schema(description = "The email address of the passport applicant.", example = "user@example.com")
	public abstract String getEmail();

	@Nullable
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@NotBlank(message = "fileNumber is required; it must not be null or blank")
	@Schema(description = "The electronic service request file number.", example = "ABCD1234", required = true)
	public abstract String getFileNumber();

	@Nullable
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@NotBlank(message = "firstName is required; it must not be null or blank")
	@Schema(description = "The first name of the passport applicant.", example = "John", required = true)
	public abstract String getFirstName();

	@Nullable
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@NotBlank(message = "lastName is required; it must not be null or blank")
	@Schema(description = "The last name of the passport applicant.", example = "Doe", required = true)
	public abstract String getLastName();

	@Nullable
	@NotNull(message = "status is required; it must not be null")
	@JsonView({ Views.POST.class, Views.GET.class, Views.PUT.class })
	@Schema(description = "The status of the passport application.", required = true)
	public abstract Status getStatus();

	@Override
	@JsonView({ Views.GET.class })
	public Links getLinks() {
		return super.getLinks();
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(@Nullable Object obj);

	@Override
	public abstract String toString();

}