package ca.gov.dtsstn.passport.api.service;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import ca.gov.dtsstn.passport.api.config.properties.GcNotifyProperties;
import ca.gov.dtsstn.passport.api.service.domain.NotificationReceipt;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final GcNotifyProperties gcNotifyProperties;

	private final RestTemplate restTemplate;

	public NotificationService(GcNotifyProperties gcNotifyProperties, RestTemplateBuilder restTemplateBuilder) {
		log.info("Creating 'notificationService' bean");

		Assert.notNull(gcNotifyProperties, "gcNotifyProperties is requred; it must not be null");
		Assert.notNull(restTemplateBuilder, "restTemplateBuilder is required; it must not be null");

		this.gcNotifyProperties = gcNotifyProperties;

		this.restTemplate = restTemplateBuilder
			.defaultHeader(HttpHeaders.AUTHORIZATION, "ApiKey-v1 %s".formatted(gcNotifyProperties.apiKey()))
			.setConnectTimeout(gcNotifyProperties.connectTimeout())
			.setReadTimeout(gcNotifyProperties.readTimeout())
			.build();
	}

	public NotificationReceipt sendFileNumberNotification(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is requird; it must not be null");
		Assert.hasText(passportStatus.getEmail(), "email is required; it must not be blank or null");

		final var email = Optional.ofNullable(passportStatus.getEmail()).orElseThrow(); // Optional<T> keeps sonar happy
		final var templateId = gcNotifyProperties.fileNumberNotification().templateId();
		final var personalization = Map.of("esrf", passportStatus.getFileNumber());
		log.trace("Request to send fileNumber notification email=[{}], parameters=[{}]", email, personalization);

		final var request = Map.of("email_address", email, "template_id", templateId, "personalisation", personalization);
		final var notificationReceipt = restTemplate.postForObject(gcNotifyProperties.baseUrl(), request, NotificationReceipt.class);
		log.debug("Notification sent to email [{}] using template [{}]", email, templateId);

		return notificationReceipt;
	}

}
