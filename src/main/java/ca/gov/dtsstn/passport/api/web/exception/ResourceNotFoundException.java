package ca.gov.dtsstn.passport.api.web.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Exception thrown when a REST resource is expected to exist, but doesn't. Typically results in an HTTP 404 response code.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
public class ResourceNotFoundException extends NestedRuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}