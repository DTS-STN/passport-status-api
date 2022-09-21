package ca.gov.dtsstn.passport.api.web.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ca.gov.dtsstn.passport.api.web.exception.PassportStatusNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.ImmutableErrorModel;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@ControllerAdvice
public class PassportStatusNotFoundAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(PassportStatusNotFoundException.class)
	protected ResponseEntity<Object> passportStatusNotFoundHandler(PassportStatusNotFoundException ex,
			WebRequest request) {
		final var errorModel = ImmutableErrorModel.builder()
		 		.errorCode("API-0404")
		 		.message(ex.getMessage())
		 		.build();

		return handleExceptionInternal(ex, errorModel,
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

}