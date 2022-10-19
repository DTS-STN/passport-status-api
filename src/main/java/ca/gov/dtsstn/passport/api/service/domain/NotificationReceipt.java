package ca.gov.dtsstn.passport.api.service.domain;

import java.io.Serializable;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Style(validationMethod = ValidationMethod.NONE)
@JsonDeserialize(as = ImmutableNotificationReceipt.class)
public interface NotificationReceipt extends AbstractDomainObject {

	@JsonProperty("content")
	NotificationReceiptContent getContent();

	@JsonProperty("id")
	String getId();

	@Nullable
	@JsonProperty("reference")
	String getReference();

	@JsonProperty("template")
	NotificationReceiptTemplate getTemplate();

	@JsonProperty("uri")
	String getUri();

	@Immutable
	@Style(validationMethod = ValidationMethod.NONE)
	@JsonDeserialize(as = ImmutableNotificationReceiptContent.class)
	public interface NotificationReceiptContent extends Serializable {

		@JsonProperty("body")
		String getBody();

		@JsonProperty("from_email")
		String getFromEmail();

		@JsonProperty("subject")
		String getSubject();

	}

	@Immutable
	@Style(validationMethod = ValidationMethod.NONE)
	@JsonDeserialize(as = ImmutableNotificationReceiptTemplate.class)
	public interface NotificationReceiptTemplate extends Serializable {

		@JsonProperty("id")
		String getId();

		@JsonProperty("uri")
		String getUri();

		@JsonProperty("version")
		String getVersion();

	}

}
