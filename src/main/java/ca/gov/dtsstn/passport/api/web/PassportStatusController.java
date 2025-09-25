package ca.gov.dtsstn.passport.api.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.config.SpringDocConfig;
import ca.gov.dtsstn.passport.api.event.PassportStatusSearchEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusSearchEvent.Result;
import ca.gov.dtsstn.passport.api.service.PassportStatusJmsService;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.model.CreateCertificateApplicationRequestModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationRepresentationModel;
import ca.gov.dtsstn.passport.api.web.model.assembler.GetCertificateApplicationRepresentationModelAssembler;
import ca.gov.dtsstn.passport.api.web.model.mapper.CertificateApplicationModelMapper;
import ca.gov.dtsstn.passport.api.web.validation.BooleanString;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@RestController
@ApiResponses.InternalServerError
@RequestMapping({ "/api/v1/passport-statuses" })
@Tag(name = "passport-statuses", description = "Endpoint to create, read, and search for passport statuses.")
public class PassportStatusController {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusController.class);

	private ApplicationEventPublisher eventPublisher;

	private final CertificateApplicationModelMapper mapper;

	private final GetCertificateApplicationRepresentationModelAssembler assembler;

	private final PassportStatusJmsService passportStatusJmsService;

	private final PassportStatusService service;

	private final SpringValidatorAdapter validator;

	public PassportStatusController(
			ApplicationEventPublisher eventPublisher,
			GetCertificateApplicationRepresentationModelAssembler assembler,
			CertificateApplicationModelMapper mapper,
			PassportStatusJmsService passportStatusJmsService,
			PassportStatusService service,
			SpringValidatorAdapter validator) {
		log.info("Creating 'passportStatusController' bean");

		Assert.notNull(assembler, "assembler is required; it must not be null");
		Assert.notNull(eventPublisher, "eventPublisher is required; it must not be null;");
		Assert.notNull(mapper, "mapper is required; it must not be null");
		Assert.notNull(passportStatusJmsService, "passportStatusJmsService is required; it must not be null");
		Assert.notNull(passportStatusJmsService, "passportStatusJmsService is required; it must not be null");
		Assert.notNull(service, "service is requred; it must not be null");
		Assert.notNull(validator, "validator is required; it must not be null");

		this.assembler = assembler;
		this.eventPublisher = eventPublisher;
		this.mapper = mapper;
		this.passportStatusJmsService = passportStatusJmsService;
		this.service = service;
		this.validator = validator;
	}

	/**
	 * Create a new {@link PassportStatus} in the system.
	 */
	@PostMapping({ "" })
	@ApiResponses.BadRequestError
	@ApiResponses.AccessDeniedError
	@ApiResponses.AuthenticationError
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Authorities.HasPassportStatusWriteAll
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Create a new passport status.", operationId = "passport-status-create")
	@ApiResponse(responseCode = "202", description = "The request has been accepted for processing.")
	public void create(
			@RequestBody(required = true)
			CreateCertificateApplicationRequestModel createCertificateApplicationRequest,

			@RequestParam(defaultValue = "true", required = false)
			@BooleanString(message = "async must be one of: 'true', 'false'")
			@Parameter(description = "If the request should be handled asynchronously.", schema = @Schema(allowableValues = { "false", "true" }, defaultValue = "true"))
			String async) {
		if (!BooleanUtils.toBoolean(async)) {
			log.warn("Call to unsupported operation: create(async=false)");
			throw new UnsupportedOperationException("synchronous processing not yet implemented; please set async=true");
		}

		log.debug("Performing field validations on createCertificateApplicationRequest");
		final var constraintViolations = validator.validate(createCertificateApplicationRequest);
		if (!constraintViolations.isEmpty()) { throw new ConstraintViolationException(constraintViolations); }
		log.debug("createCertificateApplicationRequest passed validation with no errors");

		final var passportStatus = mapper.toDomain(createCertificateApplicationRequest);
		log.debug("Queueing passport status: {}", passportStatus);
		passportStatusJmsService.send(passportStatus);
	}

	/**
	 * Perform a passport status search using the following fields:
	 *
	 * <ul>
	 *   <li>{@code dateOfBirth}
	 *   <li>{@code fileNumber}
	 *   <li>{@code givenName}
	 *   <li>{@code surname}
	 *
	 * This endpoint will perform some logic on the search results as follows:
	 *
	 * <ol>
	 *   <li>Perform a search using the provided parameters
	 *   <li>Check for distinct {@code applicationRegisterSid}; throw exception if <strong>more than one</strong> value is found
	 *   <li>If a distict {@code applicationRegisterSid} was found, perform a new search using the {@code applicationRegisterSid}
	 *   <li>Sort the results by {@code PassportStatus.version}, extract newest passport status, wrap in a collection and return
	 */
	@GetMapping({ "/_search" })
	@ApiResponses.BadRequestError
	@ResponseStatus(code = HttpStatus.OK)
	@ApiResponses.UnprocessableEntityError
	@ApiResponse(responseCode = "200", description = "Retrieve a paged list of all passport statuses satisfying the search criteria.")
	@Operation(summary = "Search for a passport status by fileNumber, givenName, surname and dateOfBirth.", operationId = "passport-status-search")
	public CollectionModel<GetCertificateApplicationRepresentationModel> search(
			@DateTimeFormat(iso = ISO.DATE)
			@NotNull(message = "dateOfBirth must not be null or blank")
			@PastOrPresent(message = "dateOfBirth must be a date in the past")
			@Parameter(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
			@RequestParam(required = false) LocalDate dateOfBirth,

			@NotBlank(message = "fileNumber must not be null or blank")
			@Parameter(description = "The electronic service request file number.", example = "ABCD1234", required = true)
			@RequestParam(required = false) String fileNumber,

			@NotBlank(message = "givenName must not be null or blank")
			@Parameter(description = "The given name of the passport applicant.", example = "John", required = true)
			@RequestParam(required = false) String givenName,

			@NotBlank(message = "surname must not be null or blank")
			@Parameter(description = "The surname of the passport applicant.", example = "Doe", required = true)
			@RequestParam(required = false) String surname,

			@Deprecated // This parameter will soon be removed
			@Parameter(description = "If the query should return a single unique result.", required = false)
			@RequestParam(defaultValue = "true") boolean unique) {
		log.debug("Performing passport status search using terms {}", List.of(dateOfBirth, fileNumber, givenName, surname));
		final var initialSearchResults = service.fileNumberSearch(dateOfBirth, fileNumber, givenName, surname);
		log.debug("{} results returned for search terms {}", initialSearchResults.size(), List.of(dateOfBirth, fileNumber, givenName, surname));

		log.debug("Checking results for distinct applicationRegisterSids");
		final var applicationRegisterSids = initialSearchResults.stream().map(PassportStatus::getApplicationRegisterSid).distinct().toList();
		final var nApplicationRegisterSids = applicationRegisterSids.size();
		log.debug("Number of distinct applicationRegisterSids: {}", nApplicationRegisterSids);

		final var searchEventBuilder = PassportStatusSearchEvent.builder().dateOfBirth(dateOfBirth).fileNumber(fileNumber).givenName(givenName).surname(surname);

		if (nApplicationRegisterSids > 1) {
			log.warn("Search query returned non-unique applicationRegisterSid result: {}", List.of(dateOfBirth, fileNumber, givenName, surname));
			eventPublisher.publishEvent(searchEventBuilder.result(Result.NON_UNIQUE).applicationRegisterSids(applicationRegisterSids).build());
			throw new NonUniqueResourceException("Search query returned non-unique applicationRegisterSid result");
		}

		log.debug("Performing applicationRegisterSid search");
		final var passportStatuses = applicationRegisterSids.stream().findFirst().map(service::applicationRegisterSidSearch).orElse(Collections.emptyList());
		final var passportStatus = passportStatuses.stream().sorted(byVersionDesc()).findFirst();
		log.debug("applicationRegisterSid search produced {} results; newest status: {}", passportStatuses.size(), passportStatus);

		if (passportStatuses.isEmpty()) {
			searchEventBuilder.result(Result.MISS);
			eventPublisher.publishEvent(searchEventBuilder.build());
		}
		else {
			searchEventBuilder.result(Result.HIT);
			passportStatus.ifPresent(searchEventBuilder::passportStatus);
			eventPublisher.publishEvent(searchEventBuilder.build());
		}

		final var selfLink = linkTo(methodOn(getClass()).search(dateOfBirth, fileNumber, givenName, surname, unique)).withSelfRel();
		final var collection = assembler.toCollectionModel(passportStatus.map(List::of).orElse(Collections.emptyList())).add(selfLink);
		return assembler.wrapCollection(collection, GetCertificateApplicationRepresentationModel.class);
	}

	/**
	 * Perform a passport status search using the following fields:
	 *
	 * <ul>
	 *   <li>{@code dateOfBirth}
	 *   <li>{@code fileNumber}
	 *   <li>{@code singleName aka surname}
	 *
	 * This endpoint is used for individuals who have a single name without a given name. Given name must be explicitly null in the repository.
	 * 
	 * This endpoint will perform some logic on the search results as follows:
	 *
	 * <ol>
	 *   <li>Perform a search using the provided parameters
	 *   <li>Check for distinct {@code applicationRegisterSid}; throw exception if <strong>more than one</strong> value is found
	 *   <li>If a distict {@code applicationRegisterSid} was found, perform a new search using the {@code applicationRegisterSid}
	 *   <li>Sort the results by {@code PassportStatus.version}, extract newest passport status, wrap in a collection and return
	 */
	@GetMapping({ "/_search-single-name" })
	@ApiResponses.BadRequestError
	@ResponseStatus(code = HttpStatus.OK)
	@ApiResponses.UnprocessableEntityError
	@ApiResponse(responseCode = "200", description = "Retrieve a paged list of all passport statuses satisfying the search criteria.")
	@Operation(summary = "Search for a passport status by fileNumber, singleName and dateOfBirth.", operationId = "passport-status-search")
	public CollectionModel<GetCertificateApplicationRepresentationModel> searchSingleName(
			@DateTimeFormat(iso = ISO.DATE)
			@NotNull(message = "dateOfBirth must not be null or blank")
			@PastOrPresent(message = "dateOfBirth must be a date in the past")
			@Parameter(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
			@RequestParam(required = false) LocalDate dateOfBirth,

			@NotBlank(message = "fileNumber must not be null or blank")
			@Parameter(description = "The electronic service request file number.", example = "ABCD1234", required = true)
			@RequestParam(required = false) String fileNumber,

			@NotBlank(message = "singleName must not be null or blank")
			@Parameter(description = "The singleName of the passport applicant.", example = "Spock", required = true)
			@RequestParam(required = false) String singleName,

			@Deprecated // This parameter will soon be removed
			@Parameter(description = "If the query should return a single unique result.", required = false)
			@RequestParam(defaultValue = "true") boolean unique) {
		log.debug("Performing passport status search using terms {}", List.of(dateOfBirth, fileNumber, singleName));
		final var initialSearchResults = service.fileNumberSearchSingleName(dateOfBirth, fileNumber, singleName);
		log.debug("{} results returned for search terms {}", initialSearchResults.size(), List.of(dateOfBirth, fileNumber, singleName));

		log.debug("Checking results for distinct applicationRegisterSids");
		final var applicationRegisterSids = initialSearchResults.stream().map(PassportStatus::getApplicationRegisterSid).distinct().toList();
		final var nApplicationRegisterSids = applicationRegisterSids.size();
		log.debug("Number of distinct applicationRegisterSids: {}", nApplicationRegisterSids);

		final var searchEventBuilder = PassportStatusSearchEvent.builder().dateOfBirth(dateOfBirth).fileNumber(fileNumber).surname(singleName);

		if (nApplicationRegisterSids > 1) {
			log.warn("Search query returned non-unique applicationRegisterSid result: {}", List.of(dateOfBirth, fileNumber, singleName));
			eventPublisher.publishEvent(searchEventBuilder.result(Result.NON_UNIQUE).applicationRegisterSids(applicationRegisterSids).build());
			throw new NonUniqueResourceException("Search query returned non-unique applicationRegisterSid result");
		}

		log.debug("Performing applicationRegisterSid search");
		final var passportStatuses = applicationRegisterSids.stream().findFirst().map(service::applicationRegisterSidSearch).orElse(Collections.emptyList());
		final var passportStatus = passportStatuses.stream().sorted(byVersionDesc()).findFirst();
		log.debug("applicationRegisterSid search produced {} results; newest status: {}", passportStatuses.size(), passportStatus);

		if (passportStatuses.isEmpty()) {
			searchEventBuilder.result(Result.MISS);
			eventPublisher.publishEvent(searchEventBuilder.build());
		}
		else {
			searchEventBuilder.result(Result.HIT);
			passportStatus.ifPresent(searchEventBuilder::passportStatus);
			eventPublisher.publishEvent(searchEventBuilder.build());
		}

		final var selfLink = linkTo(methodOn(getClass()).searchSingleName(dateOfBirth, fileNumber, singleName, unique)).withSelfRel();
		final var collection = assembler.toCollectionModel(passportStatus.map(List::of).orElse(Collections.emptyList())).add(selfLink);
		return assembler.wrapCollection(collection, GetCertificateApplicationRepresentationModel.class);
	}

	protected Comparator<PassportStatus> byVersionDesc() {
		return Comparator.comparingLong(PassportStatus::getVersion).reversed();
	}
}
