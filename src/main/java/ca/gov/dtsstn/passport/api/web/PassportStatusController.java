package ca.gov.dtsstn.passport.api.web;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

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
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationModelMapper;
import ca.gov.dtsstn.passport.api.web.model.CreateCertificateApplicationRequestModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationResponseModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationResponseModelAssembler;
import ca.gov.dtsstn.passport.api.web.model.error.AccessDeniedErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.AuthenticationErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.BadRequestErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.InternalServerErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ResourceNotFoundErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.UnprocessableEntityErrorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@RestController
@RequestMapping({ "/api/v1/passport-statuses" })
@Tag(name = "Passport Statuses", description = "Passport Status API")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(schema = @Schema(implementation = InternalServerErrorModel.class)) })
public class PassportStatusController {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusController.class);

	private final CertificateApplicationModelMapper certificateApplicationModelMapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	private final GetCertificateApplicationResponseModelAssembler getCertificateApplicationResponseModelAssembler;

	private final PassportStatusJmsService passportStatusJmsService;

	private final PassportStatusService service;

	public PassportStatusController(
			GetCertificateApplicationResponseModelAssembler getCertificateApplicationResponseModelAssembler,
			PassportStatusJmsService passportStatusJmsService,
			PassportStatusService service) {
		Assert.notNull(getCertificateApplicationResponseModelAssembler, "getCertificateApplicationResponseModelAssembler is required; it must not be null");
		Assert.notNull(passportStatusJmsService, "passportStatusJmsService is required; it must not be null");
		Assert.notNull(service, "service is requred; it must not be null");
		this.getCertificateApplicationResponseModelAssembler = getCertificateApplicationResponseModelAssembler;
		this.passportStatusJmsService = passportStatusJmsService;
		this.service = service;
	}

	@PostMapping({ "" })
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Authorities.HasPassportStatusWriteAll
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Create a new passport status.")
	@ApiResponse(responseCode = "202", description = "The request has been accepted for processing.")
	@ApiResponse(responseCode = "400", description = "Returned if the server cannot or will not process the request due to something that is perceived to be a client error.", content = { @Content(schema = @Schema(implementation = BadRequestErrorModel.class)) })
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	public void create(Authentication authentication, @RequestBody @Validated CreateCertificateApplicationRequestModel passportStatusCreateRequestModel, @Parameter(description = "If the request should be handled asynchronously.") @RequestParam(defaultValue = "true", required = false) boolean async) {
		if (!async) { throw new UnsupportedOperationException("synchronous processing not yet implemented; please set async=true"); }
		passportStatusJmsService.send(certificateApplicationModelMapper.toDomain(passportStatusCreateRequestModel));
	}

	@GetMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	@Authorities.HasPassportStatusRead
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Retrieves a passport status by its internal database ID.")
	@ApiResponse(responseCode = "200", description = "Returns an instance of a passport status.")
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	@ApiResponse(responseCode = "404", description = "Returned if the passport status was not found or the user does not have access to the resource.", content = { @Content(schema = @Schema(implementation = ResourceNotFoundErrorModel.class)) })
	public GetCertificateApplicationResponseModel get(@Parameter(description = "The internal database ID that represents the passport status.") @PathVariable String id) {
		return service.read(id).map(getCertificateApplicationResponseModelAssembler::toModel).orElseThrow(() -> new ResourceNotFoundException("Could not find the passport status with id=[" + id + "]"));
	}

	@GetMapping({ "" })
	@ResponseStatus(HttpStatus.OK)
	@Authorities.HasPassportStatusReadAll
	@SecurityRequirement(name = SpringDocConfig.HTTP)
	@SecurityRequirement(name = SpringDocConfig.OAUTH)
	@Operation(summary = "Retrieve a paged list of all passport statuses.")
	@ApiResponse(responseCode = "200", description = "Retrieves all the passport statuses available to the user.")
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	public PagedModel<GetCertificateApplicationResponseModel> getAll(Authentication authentication, @SortDefault({ "fileNumber" }) @ParameterObject Pageable pageable) {
		return getCertificateApplicationResponseModelAssembler.toModel(service.readAll(pageable));
	}

	/*
	 * Note: @Parameter(required = true) lets Swagger render correctly
	 *       @RequestParam(required = false) lets JSR validation handle the validation (instead of Spring's web binder)
	 */
	@GetMapping({ "/_search" })
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Search for a passport status by fileNumber, firstName, lastName and dateOfBirth.")
	@ApiResponse(responseCode = "200", description = "Retrieve a paged list of all passport statuses satisfying the search criteria.")
	@ApiResponse(responseCode = "400", description = "Returned if any of the request parameters are not valid.", content = { @Content(schema = @Schema(implementation = BadRequestErrorModel.class))} )
	@ApiResponse(responseCode = "422", description = "Returned if uniqueness was requested but the search query returned non-unique results.", content = { @Content(schema = @Schema(implementation = UnprocessableEntityErrorModel.class)) })
	public CollectionModel<GetCertificateApplicationResponseModel> search(
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
		final var collection = getCertificateApplicationResponseModelAssembler.toCollectionModel(passportStatuses).add(selfLink);

		return getCertificateApplicationResponseModelAssembler.wrapCollection(collection, GetCertificateApplicationResponseModel.class);
	}

}
