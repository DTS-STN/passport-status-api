package ca.gov.dtsstn.passport.api.web;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.assembler.PassportStatusModelAssembler;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.ApiErrorModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestController
@RequestMapping({ "/api/v1/passport-statuses" })
@Tag(name = "passport-status", description = "Passport Status API")
public class PassportStatusController {

	private final PassportStatusModelAssembler passportStatusModelAssembler;

	private final PassportStatusModelMapper passportStatusModelMapper;

	private final PassportStatusService passportStatusService;

	public PassportStatusController(PassportStatusModelAssembler passportStatusModelAssembler, PassportStatusModelMapper passportStatusModelMapper, PassportStatusService passportStatusService) {
		Assert.notNull(passportStatusModelAssembler, "passportStatusModelAssembler is required; it must not be null");
		Assert.notNull(passportStatusModelMapper, "passportStatusModelMapper is required; it must not be null");
		Assert.notNull(passportStatusService, "passportStatusService is requred; it must not be null");
		this.passportStatusModelAssembler = passportStatusModelAssembler;
		this.passportStatusModelMapper = passportStatusModelMapper;
		this.passportStatusService = passportStatusService;
	}

	@Operation(summary = "Find passport status by ID")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation"),
		@ApiResponse(responseCode = "404", description = "Passport status not found", content = @Content(schema = @Schema(implementation = ApiErrorModel.class)))
	})
	@GetMapping({ "/{id}" })
	public PassportStatusModel get(@Parameter(description = "ID of passport status that needs to be fetched", required = true) @PathVariable String id) {
		return passportStatusService.read(id)
			.map(passportStatusModelMapper::fromDomain)
			.map(passportStatusModelAssembler::toModel)
			.orElseThrow(() -> new ResourceNotFoundException("Could not find the passport status with id=[" + id + "]"));
	}

	@Operation(summary = "Find all passport statuses paged")
	@ApiResponses(value = @ApiResponse(responseCode = "200", description = "Successful operation"))
	@GetMapping({ /* root */ })
	public PagedModel<PassportStatusModel> getAll(@ParameterObject Pageable pageable) {
		return passportStatusModelAssembler.toPagedModel(passportStatusService.readAll(pageable));
	}

	@Operation(summary = "Search passport status by fileNumber, firstName, lastName and dateOfBirth")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successful operation"),
		@ApiResponse(responseCode = "400", description = "Invalid input value", content = @Content(schema = @Schema(implementation = ApiErrorModel.class))),
		@ApiResponse(responseCode = "404", description = "Passport status not found", content = @Content(schema = @Schema(implementation = ApiErrorModel.class)))
	})
	@GetMapping({ "/_search" })
	@ResponseStatus(code = HttpStatus.OK)
	public PassportStatusModel search(@ParameterObject @Validated PassportStatusSearchModel passportStatusSearchModel) {

		final var passportStatusProbe = passportStatusModelMapper.toDomain(passportStatusSearchModel);
		final var page = passportStatusService.search(passportStatusProbe, Pageable.unpaged())
			.map(passportStatusModelMapper::fromDomain)
			.map(passportStatusModelAssembler::toModel);

		if (page.isEmpty()) { throw new ResourceNotFoundException("Search query returned no results"); }
		if (page.getNumberOfElements() > 1) { throw new ResourceNotFoundException("Search query returned non-unique results"); }

		return page.getContent().get(0);
	}

}
