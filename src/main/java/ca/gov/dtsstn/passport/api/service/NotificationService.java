package ca.gov.dtsstn.passport.api.service;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.config.CacheConfig;
import ca.gov.dtsstn.passport.api.config.properties.GcNotifyProperties;
import ca.gov.dtsstn.passport.api.service.domain.NotificationReceipt;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class NotificationService {

	public enum PreferredLanguage { ENGLISH, FRENCH }

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final GcNotifyProperties gcNotifyProperties;

	private final RestTemplateBuilder restTemplateBuilder;

	public NotificationService(GcNotifyProperties gcNotifyProperties, RestTemplateBuilder restTemplateBuilder) {
		log.info("Creating 'notificationService' bean");

		Assert.notNull(gcNotifyProperties, "gcNotifyProperties is requred; it must not be null");
		Assert.notNull(restTemplateBuilder, "restTemplateBuilder is required; it must not be null");

		this.gcNotifyProperties = gcNotifyProperties;
		this.restTemplateBuilder = restTemplateBuilder;
	}

	/**
	 * Sends an email notification (via GC Notify) to a target recipient.
	 * <p>
	 * This method is marked as {@code @Cacheable} to reduce the number of emails sent for
	 * a given ESRF number. Think of it as a poor-man's rate-limiter/spam-reducer.
	 *
	 * @see application.yaml
	 * @see CacheConfig#esrfEmailsCache
	 */
	@Cacheable(cacheNames = { "esrf-emails" }, key = "#passportStatus.fileNumber")
	public NotificationReceipt sendFileNumberNotification(PassportStatus passportStatus, PreferredLanguage preferredLanguage) {
		Assert.notNull(passportStatus, "passportStatus is requird; it must not be null");
		Assert.hasText(passportStatus.getEmail(), "email is required; it must not be blank or null");

		final var restTemplate = restTemplateBuilder
			.defaultHeader(HttpHeaders.AUTHORIZATION, "ApiKey-v1 %s".formatted(getApiKey(preferredLanguage)))
			.setConnectTimeout(gcNotifyProperties.connectTimeout())
			.setReadTimeout(gcNotifyProperties.readTimeout())
			.build();

		final var email = Optional.ofNullable(passportStatus.getEmail()).orElseThrow(); // Optional<T> keeps sonar happy
		final var templateId = getTemplateId(preferredLanguage);
		final var personalization = Map.of("fileNumber", passportStatus.getFileNumber(), "givenName", passportStatus.getGivenName(), "surname", passportStatus.getSurname());
		log.trace("Request to send fileNumber notification email=[{}], parameters=[{}]", email, personalization);

		final var request = Map.of("email_address", email, "template_id", templateId, "personalisation", personalization);
		final var notificationReceipt = restTemplate.postForObject(gcNotifyProperties.baseUrl(), request, NotificationReceipt.class);
		log.debug("Notification sent to email [{}] using template [{}]", email, templateId);

		return notificationReceipt;
	}

	private String getTemplateId(PreferredLanguage preferredLanguage) {
		return switch (preferredLanguage) {
			case ENGLISH -> gcNotifyProperties.fileNumberNotification().englishTemplateId();
			case FRENCH -> gcNotifyProperties.fileNumberNotification().frenchTemplateId();
			default -> throw new IllegalArgumentException("Unknown preferred language value " + preferredLanguage);
		};
	}

	private String getApiKey(PreferredLanguage preferredLanguage) {
		return switch (preferredLanguage) {
			case ENGLISH -> gcNotifyProperties.englishApiKey();
			case FRENCH -> gcNotifyProperties.frenchApiKey();
			default -> throw new IllegalArgumentException("Unknown preferred language value " + preferredLanguage);
		};
	}

}
