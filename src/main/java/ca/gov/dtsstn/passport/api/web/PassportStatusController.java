package ca.gov.dtsstn.passport.api.web;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.assembler.PassportStatusModelAssembler;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestController
@RequestMapping({ "/api/v1/passport-statuses" })
public class PassportStatusController {

	private final PassportStatusModelAssembler passportStatusModelAssembler;

	private final PassportStatusService passportStatusService;

	public PassportStatusController(PassportStatusModelAssembler passportStatusModelAssembler, PassportStatusService passportStatusService) {
		Assert.notNull(passportStatusModelAssembler, "passportStatusModelAssembler is required; it must not be null");
		Assert.notNull(passportStatusService, "passportStatusService is requred; it must not be null");
		this.passportStatusModelAssembler = passportStatusModelAssembler;
		this.passportStatusService = passportStatusService;
	}

	@GetMapping({ "/{id}" })
	public PassportStatusModel get(@PathVariable String id) {
		return passportStatusModelAssembler.toModel(passportStatusService.read(id).orElseThrow());
	}

	@GetMapping({ /* root */ })
	public CollectionModel<PassportStatusModel> getAll(@ParameterObject Pageable pageable) {
		return passportStatusModelAssembler.toPagedModel(passportStatusService.readAll(pageable));
	}

}
