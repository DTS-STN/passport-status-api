package ca.gov.dtsstn.passport.api.service.domain;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@JsonDeserialize(as = ImmutableNotificationReceipt.class)
public interface NotificationReceipt extends Serializable {

	@Nullable
	@JsonProperty("content")
	NotificationReceiptContent getContent();

	@Nullable
	@JsonProperty("id")
	String getId();

	@Nullable
	@JsonProperty("reference")
	String getReference();

	@Nullable
	@JsonProperty("scheduled_for")
	String getScheduledFor();

	@Nullable
	@JsonProperty("template")
	NotificationReceiptTemplate getTemplate();

	@Nullable
	@JsonProperty("uri")
	String getUri();

	@Immutable
	@JsonDeserialize(as = ImmutableNotificationReceiptContent.class)
	public interface NotificationReceiptContent extends Serializable {

		@Nullable
		@JsonProperty("body")
		String getBody();

		@Nullable
		@JsonProperty("from_email")
		String getFromEmail();

		@Nullable
		@JsonProperty("subject")
		String getSubject();

	}

	@Immutable
	@JsonDeserialize(as = ImmutableNotificationReceiptTemplate.class)
	public interface NotificationReceiptTemplate extends Serializable {

		@Nullable
		@JsonProperty("id")
		String getId();

		@Nullable
		@JsonProperty("uri")
		String getUri();

		@Nullable
		@JsonProperty("version")
		String getVersion();

	}

}
