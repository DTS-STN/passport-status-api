package ca.gov.dtsstn.passport.api.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;

/**
 * A Spring {@link RepresentationModelAssembler} to add HATEOAS metadata to a {@link PassportStatusModel}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusModelAssembler extends AbstractModelAssembler<PassportStatus, PassportStatusModel> {

	private final PassportStatusModelMapper passportStatusModelMapper;

	public PassportStatusModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler, PassportStatusModelMapper passportStatusModelMapper) {
		super(PassportStatusController.class, PassportStatusModel.class, pagedResourcesAssembler);

		Assert.notNull(passportStatusModelMapper, "passportStatusModelMapper is required; it must not be null");
		this.passportStatusModelMapper = passportStatusModelMapper;
	}

	@Override
	protected PassportStatusModel instantiateModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		return passportStatusModelMapper.fromDomain(passportStatus);
	}

	@Override
	@SuppressWarnings({ "java:S4449" })
	public PassportStatusModel toModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.hasText(passportStatus.getId(), "passportStatus.id is required; it must not be blank or null");

		final var searchQueryTemplate = "?dateOfBirth={dateOfBirth}&fileNumber={fileNumber}&firstName={firstName}&lastName={lastName}";
		final var searchMethod = methodOn(PassportStatusController.class).search(null, null, true);
		final var searchLink = linkTo(searchMethod).slash(searchQueryTemplate).withRel("search").expand(passportStatus.getDateOfBirth(), passportStatus.getFileNumber(), passportStatus.getFirstName(), passportStatus.getLastName());

		return createModelWithId(passportStatus.getId(), passportStatus).add(searchLink);
	}

}