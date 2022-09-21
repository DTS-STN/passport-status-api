package ca.gov.dtsstn.passport.api.service.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
public class NonUniqueResultException extends NestedRuntimeException {

	public NonUniqueResultException(String message) {
		super(message);
	}

	public NonUniqueResultException(String message, Throwable cause) {
		super(message, cause);
	}

}
