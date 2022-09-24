package ca.gov.dtsstn.passport.api.web.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
public class NonUniqueResourceException extends NestedRuntimeException {

	public NonUniqueResourceException(String message) {
		super(message);
	}

	public NonUniqueResourceException(String message, Throwable cause) {
		super(message, cause);
	}

}