package ca.gov.dtsstn.passport.api.service;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import ca.gov.dtsstn.passport.api.config.properties.GcNotifyProperties;
import ca.gov.dtsstn.passport.api.service.domain.NotificationReceipt;
import ca.gov.dtsstn.passport.api.service.event.ImmutableNotificationSentEvent;
import io.micrometer.core.annotation.Timed;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final ApplicationEventPublisher eventPublisher;

	private final GcNotifyProperties gcNotifyProperties;

	private final RestTemplate restTemplate;

	public NotificationService(ApplicationEventPublisher eventPublisher, GcNotifyProperties gcNotifyProperties, RestTemplateBuilder restTemplateBuilder) {
		log.info("Creating 'notificationService' bean");

		Assert.notNull(eventPublisher, "eventPublisher is required; it must not be null");
		Assert.notNull(gcNotifyProperties, "gcNotifyProperties is requred; it must not be null");
		Assert.notNull(restTemplateBuilder, "restTemplateBuilder is required; it must not be null");

		this.eventPublisher = eventPublisher;
		this.gcNotifyProperties = gcNotifyProperties;
		this.restTemplate = restTemplateBuilder
			.defaultHeader(HttpHeaders.AUTHORIZATION, "ApiKey-v1 " + gcNotifyProperties.apiKey())
			.setConnectTimeout(gcNotifyProperties.connectTimeout())
			.setReadTimeout(gcNotifyProperties.readTimeout())
			.build();
	}

	@Timed
	public NotificationReceipt send(String emailAddress, String templateId, @Nullable Map<String, String> parameters) {
		log.trace("Request to send notificationl emailAddress=[{}], templateId=[{}], parameters=[{}]", emailAddress, templateId, parameters);

		Assert.hasText(emailAddress, "emailAddress is required; it must not be blank or null");
		Assert.hasText(templateId, "templateId is required, it must not be blank or null");

		final var personalization = Optional.ofNullable(parameters).orElse(Map.of());
		final var request = Map.of("email_address", emailAddress, "template_id", templateId, "personalisation", personalization);

		final var notificationReceipt = restTemplate.postForObject(gcNotifyProperties.baseUrl(), request, NotificationReceipt.class);
		log.debug("Notification sent to email [{}] using template [{}]", emailAddress, templateId);

		final var notificationSentEventBuilder = ImmutableNotificationSentEvent.builder().email(emailAddress).templateId(templateId);
		Optional.ofNullable(parameters).ifPresent(notificationSentEventBuilder::parameters);
		eventPublisher.publishEvent(notificationSentEventBuilder.build());

		return notificationReceipt;
	}

}
