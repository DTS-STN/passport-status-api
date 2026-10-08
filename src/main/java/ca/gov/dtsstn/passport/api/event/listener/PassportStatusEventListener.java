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
import ca.gov.dtsstn.passport.api.data.entity.EventLogEntityBuilder;
import ca.gov.dtsstn.passport.api.data.entity.EventLogEntity.EventLogType;
import ca.gov.dtsstn.passport.api.event.PassportStatusCreateConflictEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusDeletedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusReadEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusSearchEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusUpdatedEvent;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusEventListener {

	private final Logger log = LoggerFactory.getLogger(PassportStatusEventListener.class);

	private final EventLogRepository eventLogRepository;

	private final JsonMapper objectMapper;

	public PassportStatusEventListener(EventLogRepository eventLogRepository, JsonMapper jsonMapper) {
		Assert.notNull(eventLogRepository, "eventLogRepository is required; it must not be null");
		this.eventLogRepository = eventLogRepository;

		this.objectMapper = jsonMapper.rebuild()
			.configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.build();
	}

	@Async
	@EventListener({ PassportStatusCreateConflictEvent.class })
	public void handleCreated(PassportStatusCreateConflictEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.CREATE_STATUS_CONFLICT)
			.description("Passport status create conflict")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: status create conflict - ID: " + event.getEntity().getId());
	}

	@Async
	@EventListener({ PassportStatusCreatedEvent.class })
	public void handleCreated(PassportStatusCreatedEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.CREATE_STATUS_SUCCESS)
			.description("Passport status create success")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: status created - ID: " + event.getEntity().getId());
	}

	@Async
	@EventListener
	public void handleRead(PassportStatusReadEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.READ_STATUS_SUCCESS)
			.description("Passport status read success")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: status read - ID: " + event.getEntity().getId());
	}

	@Async
	@EventListener({ PassportStatusUpdatedEvent.class })
	public void handleUpdated(PassportStatusUpdatedEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.UPDATE_STATUS_SUCCESS)
			.description("Passport status update success")
			.details(objectMapper.writeValueAsString(event))
			.build());

		log.info("Event: status updated - Updated ID: " + event.getUpdatedEntity().getId());
	}

	@Async
	@EventListener({ PassportStatusDeletedEvent.class })
	public void handleDeleted(PassportStatusDeletedEvent event) throws JacksonException {
		eventLogRepository.save(new EventLogEntityBuilder()
			.eventType(EventLogType.DELETE_STATUS_SUCCESS)
			.description("Passport status delete success")
			.details(objectMapper.writeValueAsString(event))
			.build());

		// Setting this as warning, since deletion should only be happening as part of
		// our retention policy so if this pops up unexpectedly we don't want to lose it
		// in the noise.
		log.warn("Event: status deleted - ID: " + event.getEntity().getId());
	}

	@Async
	@EventListener({ PassportStatusSearchEvent.class })
	public void handleSearch(PassportStatusSearchEvent event) throws JacksonException {
		switch (event.getResult()) {
			case HIT -> eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.SEARCH_STATUS_HIT)
				.description("Passport status search hit")
				.details(objectMapper.writeValueAsString(event))
				.build());

			case MISS -> eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.SEARCH_STATUS_MISS)
				.description("Passport status search miss")
				.details(objectMapper.writeValueAsString(event))
				.build());

			case NON_UNIQUE -> eventLogRepository.save(new EventLogEntityBuilder()
				.eventType(EventLogType.SEARCH_STATUS_NON_UNIQUE)
				.description("Passport status search non-unique")
				.details(objectMapper.writeValueAsString(event))
				.build());

			default -> log.warn("PassportStatusSearchEvent {} result is not implemented", event.getResult());
		}

		log.info("Event: Search result - Result: " + event.getResult().toString());
	}

}
