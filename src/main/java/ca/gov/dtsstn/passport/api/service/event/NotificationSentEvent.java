package ca.gov.dtsstn.passport.api.service.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
public interface NotificationSentEvent extends Serializable {

	String getEmail();

	String getTemplateId();

	@Default
	default Map<String, String> getParameters() {
		return Map.of();
	}

	@Default
	default Instant getTimestamp() {
		return Instant.now();
	}

}
