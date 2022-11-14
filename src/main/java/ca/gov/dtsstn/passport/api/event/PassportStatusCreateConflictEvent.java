package ca.gov.dtsstn.passport.api.event;

import java.io.Serializable;
import java.time.Instant;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusCreateConflictEvent.Builder;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface PassportStatusCreateConflictEvent extends Serializable {

	static Builder builder() {
		return ImmutablePassportStatusCreateConflictEvent.builder();
	}

	@Parameter
	PassportStatus getEntity();

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
