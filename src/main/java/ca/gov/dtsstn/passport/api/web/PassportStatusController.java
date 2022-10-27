package ca.gov.dtsstn.passport.api.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.config.SpringDocConfig;
import ca.gov.dtsstn.passport.api.service.PassportStatusJmsService;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.annotation.Authorities;
import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.CreateCertificateApplicationRequestModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationRepresentationModel;
import ca.gov.dtsstn.passport.api.web.model.assembler.GetCertificateApplicationRepresentationModelAssembler;
import ca.gov.dtsstn.passport.api.web.model.mapper.CertificateApplicationModelMapper;
import ca.gov.dtsstn.passport.api.web.validation.Boolean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@RestController
@ApiResponses.InternalServerError
@RequestMapping({ "/api/v1/passport-statuses" })
@Tag(name = "Passport Statuses", description = "Passport Status API")
public class PassportStatusController {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusController.class);

	private final CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	private final GetCertificateApplicationRepresentationModelAssembler assembler;

	private final PassportStatusJmsService passportStatusJmsService;

	private final PassportStatusService service;

	public PassportStatusController(GetCertificateApplicationRepresentationModelAssembler assembler, PassportStatusJmsService passportStatusJmsService, PassportStatusService service) {
		Assert.notNull(assembler, "assembler is required; it must not be null");
		Assert.notNull(passportStatusJmsService, "passportStatusJmsService is required; it must not be null");
		Assert.notNull(service, "service is requred; it must not be null");
		this.assembler = assembler;
		this.passportStatusJmsService = passportStatusJmsService;
		this.service = service;
	}

	@PostMapping({ "" })
	@ApiResponses.BadRequestError
	@ApiResponses.AccessDeniedError
	@ApiResponses.AuthenticationError
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Authorities.HasPassportStatusWriteAll
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Create a new passport status.")
	@ApiResponse(responseCode = "202", description = "The request has been accepted for processing.")
	public void create(
			Authentication authentication,

			@Valid
			@RequestBody
			CreateCertificateApplicationRequestModel passportStatusCreateRequestModel,

			@RequestParam(defaultValue = "true", required = false)
			@Boolean(message = "async must be one of: 'true', 'false'")
			@Parameter(description = "If the request should be handled asynchronously.", schema = @Schema(allowableValues = { "false", "true" }, defaultValue = "true"))
			String async) {
		if (!BooleanUtils.toBoolean(async)) {
			throw new UnsupportedOperationException("synchronous processing not yet implemented; please set async=true");
		}

		passportStatusJmsService.send(mapper.toDomain(passportStatusCreateRequestModel));
	}

	@GetMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses.AccessDeniedError
	@ApiResponses.AuthenticationError
	@ApiResponses.ResourceNotFoundError
	@Authorities.HasPassportStatusRead
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Retrieves a passport status by its internal database ID.")
	@ApiResponse(responseCode = "200", description = "Returns an instance of a passport status.")
	public GetCertificateApplicationRepresentationModel get(@Parameter(description = "The internal database ID that represents the passport status.") @PathVariable String id) {
		return service.read(id).map(assembler::toModel).orElseThrow(() -> new ResourceNotFoundException("Could not find the passport status with id=[%s]".formatted(id)));
	}

	@GetMapping({ "" })
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses.AccessDeniedError
	@ApiResponses.AuthenticationError
	@Authorities.HasPassportStatusReadAll
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Retrieve a paged list of all passport statuses.")
	@ApiResponse(responseCode = "200", description = "Retrieves all the passport statuses available to the user.")
	public PagedModel<GetCertificateApplicationRepresentationModel> getAll(Authentication authentication, @SortDefault({ "fileNumber" }) @ParameterObject Pageable pageable) {
		return assembler.toModel(service.readAll(pageable));
	}

	/*
	 * Note: @Parameter(required = true) lets Swagger render correctly
	 *       @RequestParam(required = false) lets JSR validation handle the validation (instead of Spring's web binder)
	 */
	@GetMapping({ "/_search" })
	@ApiResponses.BadRequestError
	@ResponseStatus(code = HttpStatus.OK)
	@ApiResponses.UnprocessableEntityError
	@Operation(summary = "Search for a passport status by fileNumber, firstName, lastName and dateOfBirth.")
	@ApiResponse(responseCode = "200", description = "Retrieve a paged list of all passport statuses satisfying the search criteria.")
	public CollectionModel<GetCertificateApplicationRepresentationModel> search(
			@DateTimeFormat(iso = ISO.DATE)
			@NotNull(message = "dateOfBirth must not be null or blank")
			@PastOrPresent(message = "dateOfBirth must be a date in the past")
			@Parameter(description = "The date of birth of the passport applicant in ISO-8601 format.", example = "2000-01-01", required = true)
			@RequestParam(required = false) LocalDate dateOfBirth,

			@NotBlank(message = "fileNumber must not be null or blank")
			@Parameter(description = "The electronic service request file number.", example = "ABCD1234", required = true)
			@RequestParam(required = false) String fileNumber,

			@NotBlank(message = "firstName must not be null or blank")
			@Parameter(description = "The first name of the passport applicant.", example = "John", required = true)
			@RequestParam(required = false)  String firstName,

			@NotBlank(message = "lastName must not be null or blank")
			@Parameter(description = "The last name of the passport applicant.", example = "Doe", required = true)
			@RequestParam(required = false) String lastName,

			@Parameter(description = "If the query should return a single unique result.", required = false)
			@RequestParam(defaultValue = "true") boolean unique) {
		final var passportStatuses = service.fileNumberSearch(dateOfBirth, fileNumber, firstName, lastName);

		if (unique && passportStatuses.size() > 1) {
			log.warn("Search query returned non-unique results: {}", List.of(dateOfBirth, fileNumber, firstName, lastName));
			throw new NonUniqueResourceException("Search query returned non-unique results");
		}

		final var selfLink = linkTo(methodOn(getClass()).search(dateOfBirth, fileNumber, firstName, lastName, unique)).withSelfRel();
		final var collection = assembler.toCollectionModel(passportStatuses).add(selfLink);

		return assembler.wrapCollection(collection, GetCertificateApplicationRepresentationModel.class);
	}

}
