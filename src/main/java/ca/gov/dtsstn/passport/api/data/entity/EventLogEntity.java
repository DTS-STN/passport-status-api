package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;

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
@Entity(name = "EventLog")
@SuppressWarnings({ "serial" })
public class EventLogEntity extends AbstractEntity {

	@Column(length = 256, nullable = false, updatable = false)
	private String description;

	@Column(length = 65536, nullable = false, updatable = false)
	private String details;

	@Enumerated(EnumType.STRING)
	@Column(length = 32, nullable = false, updatable = false)
	private EventLogType eventType;

	@Column(length = 128, nullable = true, updatable = false)
	private String actor;

	@Column(length = 256, nullable = true, updatable = false)
	private String source;

	public EventLogEntity() {
		super();
	}

	@Builder.Constructor
	protected EventLogEntity( // NOSONAR (too many parameters)
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Boolean isNew,
			@Nullable String actor,
			@Nullable EventLogType eventType,
			@Nullable String description,
			@Nullable String details,
			@Nullable String source) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.description = description;
		this.details = details;
		this.eventType = eventType;
		this.actor = actor;
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public EventLogType getEventType() {
		return eventType;
	}

	public void setEventType(EventLogType eventType) {
		this.eventType = eventType;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
			.append("id", id)
			.append("createdBy", createdBy)
			.append("createdDate", createdDate)
			.append("lastModifiedBy", lastModifiedBy)
			.append("lastModifiedDate", lastModifiedDate)
			.append("isNew", isNew)
			.append("description", description)
			.append("details", details)
			.append("eventType", eventType)
			.append("actor", actor)
			.append("source", source)
			.toString();
	}

	public enum EventLogType {

		/*
		 * ESRF read events
		 */

		GET_ESRF_REQUEST,
		GET_ESRF_SUCCESS,
		GET_ESRF_FAIL,

		/*
		 * PassportStatus create/read events
		 */

		CREATE_STATUS_REQUEST,
		CREATE_STATUS_SUCCESS,
		CREATE_STATUS_FAIL,
		GET_STATUS_REQUEST,
		GET_STATUS_SUCCESS,
		GET_STATUS_FAIL

	}

}
