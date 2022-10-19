package ca.gov.dtsstn.passport.api.service.exception;

import org.springframework.core.NestedRuntimeException;

public class PassportStatusJmsException extends NestedRuntimeException {

	public PassportStatusJmsException(String message) {
		super(message);
	}

	public PassportStatusJmsException(String message, Throwable cause) {
		super(message, cause);
	}

}
