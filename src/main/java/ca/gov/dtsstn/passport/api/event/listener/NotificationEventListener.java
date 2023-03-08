package ca.gov.dtsstn.passport.api.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
	private final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

	private final EventLogRepository eventLogRepository;

	private final ObjectMapper objectMapper;

	public NotificationEventListener(EventLogRepository eventLogRepository) {
		Assert.notNull(eventLogRepository, "eventLogRepository is required; it must not be null");
		this.eventLogRepository = eventLogRepository;

		this.objectMapper = new ObjectMapper()
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.findAndRegisterModules();
	}

	@Async
	@EventListener({ NotificationNotSentEvent.class })
	public void handleNotificationNotSentEvent(NotificationNotSentEvent event) throws JsonProcessingException {
		eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.GET_ESRF_FAIL)
				.description("ESRF notification failure")
				.details(objectMapper.writeValueAsString(event))
				.build());

		log.info("Event: Get ESRF fail");
	}

	@Async
	@EventListener({ NotificationRequestedEvent.class })
	public void handleNotificationRequestedEvent(NotificationRequestedEvent event) throws JsonProcessingException {
		eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.GET_ESRF_REQUEST)
				.description("ESRF notification requested")
				.details(objectMapper.writeValueAsString(event))
				.build());

		log.info("Event: ESRF notification requested");
	}

	@Async
	@EventListener({ NotificationSentEvent.class })
	public void handleNotificationSentEvent(NotificationSentEvent event) throws JsonProcessingException {
		eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.GET_ESRF_SUCCESS)
				.description("ESRF notification success")
				.details(objectMapper.writeValueAsString(event))
				.build());

		log.info("Event: ESRF notification success");
	}

}
