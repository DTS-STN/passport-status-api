package ca.gov.dtsstn.passport.api.web.exception;

import org.springframework.core.NestedRuntimeException;

/**
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