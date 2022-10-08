package ca.gov.dtsstn.passport.api.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.fasterxml.jackson.annotation.JsonView;

import ca.gov.dtsstn.passport.api.config.SpringDocConfig;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.assembler.PassportStatusModelAssembler;
import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;
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
@RestController
@RequestMapping({ "/api/v1/passport-statuses" })
@Tag(name = "Passport Statuses", description = "Passport Status API")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(schema = @Schema(implementation = InternalServerErrorModel.class)) })
public class PassportStatusController {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusController.class);

	private final PassportStatusModelAssembler assembler;

	private final PassportStatusService service;

	public PassportStatusController(PassportStatusModelAssembler assembler, PassportStatusService service) {
		Assert.notNull(assembler, "assembler is required; it must not be null");
		Assert.notNull(service, "service is requred; it must not be null");
		this.assembler = assembler;
		this.service = service;
	}

	@PostMapping({ "" })
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Create a new passport status.")
	@SecurityRequirement(name = SpringDocConfig.BASIC_SECURITY)
	@ApiResponse(responseCode = "202", description = "The request has been accepted for processing.")
	@ApiResponse(responseCode = "400", description = "Returned if the server cannot or will not process the request due to something that is perceived to be a client error.", content = { @Content(schema = @Schema(implementation = BadRequestErrorModel.class)) })
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	public void create(Authentication authentication, @JsonView({ PassportStatusModel.CreateView.class }) @RequestBody @Validated PassportStatusModel passportStatus) {
		service.queueCreation(assembler.getMapper().toDomain(passportStatus));
	}

	@GetMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isAuthenticated()")
	@JsonView({ PassportStatusModel.ReadView.class })
	@SecurityRequirement(name = SpringDocConfig.BASIC_SECURITY)
	@Operation(summary = "Retrieves a passport status by its internal database ID.")
	@ApiResponse(responseCode = "200", description = "Returns an instance of a passport status.")
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	@ApiResponse(responseCode = "404", description = "Returned if the passport status was not found or the user does not have access to the resource.", content = { @Content(schema = @Schema(implementation = ResourceNotFoundErrorModel.class)) })
	public PassportStatusModel get(@Parameter(description = "The internal database ID that represents the passport status.") @PathVariable String id) {
		return service.read(id).map(assembler::toModel).orElseThrow(() -> new ResourceNotFoundException("Could not find the passport status with id=[" + id + "]"));
	}

	@GetMapping({ "" })
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isAuthenticated()")
	@JsonView({ PassportStatusModel.ReadView.class })
	@SecurityRequirement(name = SpringDocConfig.BASIC_SECURITY)
	@Operation(summary = "Retrieve a paged list of all passport statuses.")
	@ApiResponse(responseCode = "200", description = "Retrieves all the passport statuses available to the user.")
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(schema = @Schema(implementation = AuthenticationErrorModel.class)) })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(schema = @Schema(implementation = AccessDeniedErrorModel.class)) })
	public PagedModel<PassportStatusModel> getAll(Authentication authentication, @SortDefault({ "fileNumber" }) @ParameterObject Pageable pageable) {
		return assembler.toModel(service.readAll(pageable));
	}

	@GetMapping({ "/_search" })
	@ResponseStatus(code = HttpStatus.OK)
	@JsonView({ PassportStatusModel.ReadView.class })
	@Operation(summary = "Search for a passport status by fileNumber, firstName, lastName and dateOfBirth.")
	@ApiResponse(responseCode = "200", description = "Retrieve a paged list of all passport statuses satisfying the search criteria.")
	@ApiResponse(responseCode = "400", description = "Returned if any of the request parameters are not valid.", content = { @Content(schema = @Schema(implementation = BadRequestErrorModel.class))} )
	@ApiResponse(responseCode = "422", description = "Returned if uniqueness was requested but the search query returned non-unique results.", content = { @Content(schema = @Schema(implementation = UnprocessableEntityErrorModel.class)) })
	public PagedModel<PassportStatusModel> search(@SortDefault({ "fileNumber" }) @ParameterObject Pageable pageable, @ParameterObject @Validated PassportStatusSearchModel passportStatusSearchModel, @Parameter(description = "If the query should return a single unique result.") @RequestParam(defaultValue = "true") boolean unique) {
		final var page = service.search(assembler.toDomain(passportStatusSearchModel), pageable);

		if (unique && page.getNumberOfElements() > 1) {
			log.warn("Search query returned non-unique results: {}", passportStatusSearchModel);
			throw new NonUniqueResourceException("Search query returned non-unique results");
		}

		return assembler.toModel(page);
	}

}
