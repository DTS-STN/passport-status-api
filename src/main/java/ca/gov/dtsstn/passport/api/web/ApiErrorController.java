package ca.gov.dtsstn.passport.api.web;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.ImmutableApiErrorModel;

/**
 * API global error handler.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
public class ApiErrorController extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ConversionFailedException.class })
	public ResponseEntity<Object> conversionFailedHandler(ConversionFailedException ex, WebRequest request) {
		final var errorModel = ImmutableApiErrorModel.builder()
			.errorCode("API-0400")
			.message("Failed to convert value [" + ex.getValue() + "] to target type " + ex.getTargetType().getName())
			.build();

		return handleExceptionInternal(ex, errorModel, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<Object> resourceNotFoundHandler(Exception ex, WebRequest request) {
		final var errorModel = ImmutableApiErrorModel.builder()
			.errorCode("API-0404")
			.message(ex.getMessage())
			.build();

		return handleExceptionInternal(ex, errorModel, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

}