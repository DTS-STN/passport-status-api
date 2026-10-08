package ca.gov.dtsstn.passport.api.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import ca.gov.dtsstn.passport.api.data.EventLogRepository;
import ca.gov.dtsstn.passport.api.data.entity.EventLogEntity.EventLogType;
import ca.gov.dtsstn.passport.api.data.entity.EventLogEntityBuilder;
import ca.gov.dtsstn.passport.api.event.NotificationNotSentEvent;
import ca.gov.dtsstn.passport.api.event.NotificationRequestedEvent;
import ca.gov.dtsstn.passport.api.event.NotificationSentEvent;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class NotificationEventListener {
	private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

	private final EventLogRepository eventLogRepository;

	private final JsonMapper objectMapper;

	public NotificationEventListener(EventLogRepository eventLogRepository, JsonMapper jsonMapper) {
		Assert.notNull(eventLogRepository, "eventLogRepository is required; it must not be null");
		this.eventLogRepository = eventLogRepository;

		this.objectMapper = jsonMapper.rebuild()
			.configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.build();
	}

	@Async
	@EventListener({ NotificationNotSentEvent.class })
	public void handleNotificationNotSentEvent(NotificationNotSentEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.GET_ESRF_FAIL)
			.description("ESRF notification failure")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: Get ESRF fail - " + event.getReason());
	}

	@Async
	@EventListener({ NotificationRequestedEvent.class })
	public void handleNotificationRequestedEvent(NotificationRequestedEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.GET_ESRF_REQUEST)
			.description("ESRF notification requested")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: ESRF notification requested - " + event.getEmail());
	}

	@Async
	@EventListener({ NotificationSentEvent.class })
	public void handleNotificationSentEvent(NotificationSentEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.GET_ESRF_SUCCESS)
			.description("ESRF notification success")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: ESRF notification success - " + event.getEmail());
	}

}
