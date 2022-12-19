package ca.gov.dtsstn.passport.api.event.listener;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.event.NotificationNotSentEvent;
import ca.gov.dtsstn.passport.api.event.NotificationRequestedEvent;
import ca.gov.dtsstn.passport.api.event.NotificationSentEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusSearchEvent;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class MetricsGeneratingEventListener {

	private static final Logger log = LoggerFactory.getLogger(MetricsGeneratingEventListener.class);

	private final MeterRegistry meterRegistry;

	private final StatusCodeService statusCodeService;

	public MetricsGeneratingEventListener(MeterRegistry meterRegistry, StatusCodeService statusCodeService) {
		Assert.notNull(meterRegistry, "meterRegistry is required; it must not be null");
		Assert.notNull(statusCodeService, "statusCodeService is required; it must not be null");
		this.meterRegistry = meterRegistry;
		this.statusCodeService = statusCodeService;
	}

	@EventListener
	public void handleNotificationNotSentEvent(NotificationNotSentEvent event) {
		Assert.notNull(event, "event is required; it must not be null"); // NOSONAR (repeated string)
		meterRegistry.counter("esrf_requests.failed").increment();
	}

	@EventListener
	public void handleNotificationRequestedEvent(NotificationRequestedEvent event) {
		Assert.notNull(event, "event is required; it must not be null");
		meterRegistry.counter("esrf_requests").increment();
	}

	@EventListener
	public void handleNotificationSentEvent(NotificationSentEvent event) {
		Assert.notNull(event, "event is required; it must not be null");
		meterRegistry.counter("esrf_requests.sent").increment();
	}

	@EventListener
	public void handlePassportStatusCreatedEvent(PassportStatusCreatedEvent event) {
		Assert.notNull(event, "event is required; it must not be null");
		meterRegistry.counter("passport_statuses.created").increment();

		Optional.of(event)
			.map(PassportStatusCreatedEvent::getEntity)
			.map(PassportStatus::getStatusCodeId)
			.flatMap(statusCodeService::read)
			.map(StatusCode::getCode)
			.ifPresent(code -> {
				switch (code) {
					case "APPLICATION_NO_LONGER_MEETS_CRITERIA" -> meterRegistry.counter("passport_statuses.created.application_no_longer_meets_criteria").increment();
					case "FILE_BEING_PROCESSED" -> meterRegistry.counter("passport_statuses.created.file_being_processed").increment();
					case "NOT_ACCEPTABLE_FOR_PROCESSING" -> meterRegistry.counter("passport_statuses.created.not_acceptable_for_processing").increment();
					case "PASSPORT_ISSUED_READY_FOR_PICKUP" -> meterRegistry.counter("passport_statuses.created.passport_issued_ready_for_pickup").increment();
					case "PASSPORT_ISSUED_SHIPPING_CANADA_POST" -> meterRegistry.counter("passport_statuses.created.passport_issued_shipping_canada_post").increment();
					case "PASSPORT_ISSUED_SHIPPING_FEDEX" -> meterRegistry.counter("passport_statuses.created.passport_issued_shipping_fedex").increment();
					default -> log.warn("Invalid status code [{}] encountered", code);
				}
			});
	}

	@EventListener
	public void handlePassportStatusSearchEvent(PassportStatusSearchEvent event) {
		Assert.notNull(event, "event is required; it must not be null");
		meterRegistry.counter("passport_statuses.searches").increment();

		switch (event.getResult()) {
			case HIT -> meterRegistry.counter("passport_statuses.searches.hits").increment();
			case MISS -> meterRegistry.counter("passport_statuses.searches.misses").increment();
			case NON_UNIQUE -> meterRegistry.counter("passport_statuses.searches.non_unique").increment();
		}
	}

}
