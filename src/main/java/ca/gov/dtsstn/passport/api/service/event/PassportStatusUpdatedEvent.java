package ca.gov.dtsstn.passport.api.service.event;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatusUpdatedEvent extends Serializable {

	@Parameter
	PassportStatus getOriginalEntity();

	@Parameter
	PassportStatus getUpdatedEntity();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}