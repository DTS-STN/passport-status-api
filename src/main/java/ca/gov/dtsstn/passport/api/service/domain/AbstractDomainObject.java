package ca.gov.dtsstn.passport.api.service.domain;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.lang.Nullable;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface AbstractDomainObject extends Serializable {

	@Nullable
	String getId();

	@Nullable
	String getCreatedBy();

	@Nullable
	Instant getCreatedDate();

	@Nullable
	String getLastModifiedBy();

	@Nullable
	Instant getLastModifiedDate();

}
