package ca.gov.dtsstn.passport.api.web;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.assembler.PassportStatusModelAssembler;
import ca.gov.dtsstn.passport.api.web.exception.ResourceNotFoundException;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestController
@RequestMapping({ "/api/v1/passport-statuses" })
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

	@GetMapping({ "/{id}" })
	public PassportStatusModel get(@PathVariable String id) {
		return passportStatusService.read(id)
			.map(passportStatusModelMapper::fromDomain)
			.map(passportStatusModelAssembler::toModel)
			.orElseThrow(() -> new ResourceNotFoundException("Could not find the passport status with id=[" + id + "]"));
	}

	@GetMapping({ /* root */ })
	public PagedModel<PassportStatusModel> getAll(@ParameterObject Pageable pageable) {
		return passportStatusModelAssembler.toPagedModel(passportStatusService.readAll(pageable));
	}

	@GetMapping({ "/_search" })
	public PassportStatusModel search(@ParameterObject PassportStatusSearchModel passportStatusSearchModel) {
		Assert.hasText(passportStatusSearchModel.getFileNumber(), "fileNumber is required");
		Assert.hasText(passportStatusSearchModel.getFirstName(), "firstName is required");
		Assert.hasText(passportStatusSearchModel.getLastName(), "lastName is required");
		Assert.notNull(passportStatusSearchModel.getDateOfBirth(), "dateOfBirth is required");

		return passportStatusService.search(passportStatusModelMapper.toDomain(passportStatusSearchModel))
			.map(passportStatusModelMapper::fromDomain)
			.map(passportStatusModelAssembler::toModel)
			.orElseThrow(() -> new ResourceNotFoundException("Query returned no results"));
	}

}
