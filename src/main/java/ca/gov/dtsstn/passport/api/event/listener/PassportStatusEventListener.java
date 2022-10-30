package ca.gov.dtsstn.passport.api.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.event.PassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusDeletedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusReadEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusUpdatedEvent;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusEventListener {

	@Async
	@EventListener({ PassportStatusCreatedEvent.class })
	public void handleCreated(PassportStatusCreatedEvent event) {
		// TODO :: GjB :: handle this event (log to database)
	}

	@Async
	@EventListener
	public void handleRead(PassportStatusReadEvent event) {
		// TODO :: GjB :: handle this event (log to database)
	}

	@Async
	@EventListener({ PassportStatusUpdatedEvent.class })
	public void handleUpdated(PassportStatusUpdatedEvent event) {
		// TODO :: GjB :: handle this event (log to database)
	}

	@Async
	@EventListener({ PassportStatusDeletedEvent.class })
	public void handleDeleted(PassportStatusDeletedEvent event) {
		// TODO :: GjB :: handle this event (log to database)
	}

}
