package ca.gov.dtsstn.passport.api.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.error.BadRequestErrorModel.FieldValidationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableBadRequestErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableFieldValidationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableInternalServerErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableResourceNotFoundErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableUnprocessableEntityErrorModel;

/**
 * API global error handler.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiErrorHandler.class);

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var details = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
		final var validationErrors = ex.getFieldErrors().stream().map(this::toValidationError).toList();
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder().details(details).fieldValidationErrors(validationErrors);
		return handleExceptionInternal(ex, badRequestErrorBuilder.build(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return super.handleExceptionInternal(ex, badRequestErrorBuilder.build(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var details = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
		final var validationErrors = ex.getFieldErrors().stream().map(this::toValidationError).toList();
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder().details(details).fieldValidationErrors(validationErrors);
		return handleExceptionInternal(ex, badRequestErrorBuilder.build(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var resourceNotFoundErrorBuilder = ImmutableResourceNotFoundErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(resourceNotFoundErrorBuilder::details);
		return super.handleExceptionInternal(ex, resourceNotFoundErrorBuilder.build(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return super.handleExceptionInternal(ex, badRequestErrorBuilder.build(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final var message = "Invalid value for %s: %s".formatted(ex.getPropertyName(), ex.getValue());
		final var fieldValidationErrorBuilder = ImmutableFieldValidationErrorModel.builder().code("InvalidFormat").message(message);
		Optional.ofNullable(ex.getPropertyName()).ifPresent(fieldValidationErrorBuilder::field);
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder().details(List.of(message)).fieldValidationErrors(List.of(fieldValidationErrorBuilder.build()));
		return super.handleExceptionInternal(ex, badRequestErrorBuilder.build(), headers, status, request);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return super.handleExceptionInternal(ex, badRequestErrorBuilder.build(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ ConversionFailedException.class })
	public ResponseEntity<Object> handleConversionFailedException(ConversionFailedException ex, WebRequest request) {
		final var details = List.of("Failed to convert value [" + ex.getValue() + "] to target type " + ex.getTargetType().getName());
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder().details(details);
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return super.handleExceptionInternal(ex, badRequestErrorBuilder.build(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ NonUniqueResourceException.class })
	public ResponseEntity<Object> handleNonUniqueResourceException(NonUniqueResourceException ex, WebRequest request) {
		final var unprocessableEntityErrorBuilder = ImmutableUnprocessableEntityErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(unprocessableEntityErrorBuilder::details);
		return super.handleExceptionInternal(ex, unprocessableEntityErrorBuilder.build(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		final var resourceNotFoundErrorBuilder = ImmutableResourceNotFoundErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(resourceNotFoundErrorBuilder::details);
		return super.handleExceptionInternal(ex, resourceNotFoundErrorBuilder.build(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
		final var correlationId = generateCorrelationId();
		log.error("[correlationId: {}] Request processing failed; nested exception is {}: {}", correlationId, ex.getClass().getName(), ex.getMessage(), ex);
		final var internalServerErrorBuilder = ImmutableInternalServerErrorModel.builder().correlationId(correlationId);
		Optional.ofNullable(ex.getMessage()).ifPresent(internalServerErrorBuilder::details);
		return super.handleExceptionInternal(ex, internalServerErrorBuilder.build(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	protected String generateCorrelationId() {
		return UUID.randomUUID().toString();
	}

	protected FieldValidationErrorModel toValidationError(FieldError fieldError) {
		Assert.notNull(fieldError, "fieldError is required; it must not be null");
		final var fieldValidationErrorBuilder = ImmutableFieldValidationErrorModel.builder();
		Optional.ofNullable(fieldError.getCode()).ifPresent(fieldValidationErrorBuilder::code);
		Optional.ofNullable(fieldError.getField()).ifPresent(fieldValidationErrorBuilder::field);
		Optional.ofNullable(fieldError.getDefaultMessage()).ifPresent(fieldValidationErrorBuilder::message);
		return fieldValidationErrorBuilder.build();
	}

}
