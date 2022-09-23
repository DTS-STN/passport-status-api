package ca.gov.dtsstn.passport.api.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.ApiErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ApiValidationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableApiErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableApiValidationErrorModel;

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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		final List<String> details = ex.getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.toUnmodifiableList());

		final List<ApiValidationErrorModel> validationErrors = ex.getFieldErrors().stream()
			.map(error -> ImmutableApiValidationErrorModel.builder()
					.code(removeCurlyBraces(error.getCode()))
					.field(error.getField())
					.message(error.getDefaultMessage())
					.build())
				.collect(Collectors.toUnmodifiableList());

		final ApiErrorModel errorModel = ImmutableApiErrorModel.builder()
			.errorCode("API-0400")
			.message("Validation error")
			.details(details)
			.validationErrors(validationErrors)
			.build();

		return handleExceptionInternal(ex, errorModel, headers, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		final List<String> details = ex.getAllErrors().stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.toUnmodifiableList());

		final List<ApiValidationErrorModel> validationErrors = ex.getFieldErrors().stream()
			.map(error -> ImmutableApiValidationErrorModel.builder()
					.code(removeCurlyBraces(error.getCode()))
					.field(error.getField())
					.message(error.getDefaultMessage())
					.build())
				.collect(Collectors.toUnmodifiableList());

		final ApiErrorModel errorModel = ImmutableApiErrorModel.builder()
			.errorCode("API-0400")
			.message("Validation error")
			.details(details)
			.validationErrors(validationErrors)
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

	protected String removeCurlyBraces(String string) {
		final int startIndex = string.indexOf('{');
		final int endIndex = string.lastIndexOf('}');

		if (startIndex == -1 || endIndex == -1 || endIndex+1 != string.length()) { return string; }

		return string.substring(startIndex+1, endIndex);
	}

}