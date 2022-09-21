package ca.gov.dtsstn.passport.api.web.assembler;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusModelAssembler extends RepresentationModelAssemblerSupport<PassportStatus, PassportStatusModel> {

	private final PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler;

	private final PassportStatusModelMapper passportStatusModelMapper;

	public PassportStatusModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler, PassportStatusModelMapper passportStatusModelMapper) {
		super(PassportStatusController.class, PassportStatusModel.class);

		Assert.notNull(pagedResourcesAssembler, "pagedResourcesAssembler is required; it must not be null");
		Assert.notNull(passportStatusModelMapper, "passportStatusModelMapper is required; it must not be null");
		this.pagedResourcesAssembler = pagedResourcesAssembler;
		this.passportStatusModelMapper = passportStatusModelMapper;
	}

	@Override
	protected PassportStatusModel instantiateModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		return passportStatusModelMapper.fromDomain(passportStatus);
	}

	@Override
	public PassportStatusModel toModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.hasText(passportStatus.getId(), "passportStatus.id is required; it must not be blank or null");
		return createModelWithId(passportStatus.getId(), passportStatus);
	}

	public PagedModel<PassportStatusModel> toPagedModel(Page<PassportStatus> passportStatuses) {
		Assert.notNull(passportStatuses, "passportStatuses is required; it must not be null");
		return pagedResourcesAssembler.toModel(passportStatuses, this);
	}

}
