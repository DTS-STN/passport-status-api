package ca.gov.dtsstn.passport.api.service.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
