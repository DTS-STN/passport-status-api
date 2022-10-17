package ca.gov.dtsstn.passport.api.web;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.BadRequestErrorModel;
import ca.gov.dtsstn.passport.api.web.model.BadRequestErrorModel.FieldValidationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableBadRequestErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableFieldValidationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableInternalServerErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableResourceNotFoundErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableUnprocessableEntityErrorModel;
import ca.gov.dtsstn.passport.api.web.model.InternalServerErrorModel;
import ca.gov.dtsstn.passport.api.web.model.ResourceNotFoundErrorModel;
import ca.gov.dtsstn.passport.api.web.model.UnprocessableEntityErrorModel;

/**
 * API global error handler.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
public class ApiErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiErrorHandler.class);

	@ExceptionHandler({ BindException.class })
	protected ResponseEntity<BadRequestErrorModel> handleBindException(BindException ex) {
		final var details = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
		final var validationErrors = ex.getFieldErrors().stream().map(this::toValidationError).toList();
		final var errorModel = ImmutableBadRequestErrorModel.builder().details(details).fieldValidationErrors(validationErrors).build();
		return ResponseEntity.badRequest().body(errorModel);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<BadRequestErrorModel> handleConstraintViolationException(ConstraintViolationException ex) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return ResponseEntity.badRequest().body(badRequestErrorBuilder.build());
	}

	@ExceptionHandler({ ConversionFailedException.class })
	public ResponseEntity<BadRequestErrorModel> handleConversionFailedException(ConversionFailedException ex) {
		final var details = List.of("Failed to convert value [" + ex.getValue() + "] to target type " + ex.getTargetType().getName());
		final var error = ImmutableBadRequestErrorModel.builder().details(details).build();
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public ResponseEntity<BadRequestErrorModel> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return ResponseEntity.badRequest().body(badRequestErrorBuilder.build());
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, })
	public ResponseEntity<BadRequestErrorModel> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return ResponseEntity.badRequest().body(badRequestErrorBuilder.build());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<BadRequestErrorModel> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return ResponseEntity.badRequest().body(badRequestErrorBuilder.build());
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<BadRequestErrorModel> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		final var badRequestErrorBuilder = ImmutableBadRequestErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(badRequestErrorBuilder::message);
		return ResponseEntity.badRequest().body(badRequestErrorBuilder.build());
	}

	@ExceptionHandler({ NonUniqueResourceException.class })
	public ResponseEntity<UnprocessableEntityErrorModel> handleNonUniqueResourceException(NonUniqueResourceException ex) {
		final var unprocessableEntityErrorBuilder = ImmutableUnprocessableEntityErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(unprocessableEntityErrorBuilder::details);
		return ResponseEntity.unprocessableEntity().body(unprocessableEntityErrorBuilder.build());
	}

	@ExceptionHandler({ ResourceNotFoundException.class })
	public ResponseEntity<ResourceNotFoundErrorModel> handleResourceNotFoundException(ResourceNotFoundException ex) {
		final var resourceNotFoundErrorBuilder = ImmutableResourceNotFoundErrorModel.builder();
		Optional.ofNullable(ex.getMessage()).ifPresent(resourceNotFoundErrorBuilder::details);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundErrorBuilder.build());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<InternalServerErrorModel> handleGenericException(Exception ex) {
		final var correlationId = generateCorrelationId();
		log.error("[correlationId: {}] Request processing failed; nested exception is {}: {}", correlationId, ex.getClass().getName(), ex.getMessage(), ex);
		final var error = ImmutableInternalServerErrorModel.builder().details(ex.getMessage()).correlationId(correlationId).build();
		return ResponseEntity.internalServerError().body(error);
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
