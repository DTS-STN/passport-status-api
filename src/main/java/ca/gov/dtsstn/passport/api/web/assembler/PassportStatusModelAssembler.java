package ca.gov.dtsstn.passport.api.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.mapstruct.factory.Mappers;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

/**
 * A Spring {@link RepresentationModelAssembler} to add HATEOAS metadata to a {@link PassportStatusModel}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusModelAssembler extends AbstractModelAssembler<PassportStatus, PassportStatusModel> {

	private final PassportStatusModelMapper mapper = Mappers.getMapper(PassportStatusModelMapper.class);

	public PassportStatusModelAssembler(PagedResourcesAssembler<PassportStatus> pagedAssembler) {
		super(PassportStatusController.class, PassportStatusModel.class, pagedAssembler);
	}

	@Override
	protected PassportStatusModel instantiateModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		return mapper.fromDomain(passportStatus);
	}

	public PassportStatus toDomain(PassportStatusSearchModel passportStatusSearchModel) {
		return mapper.toDomain(passportStatusSearchModel);
	}

	@Override
	public PassportStatusModel toModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.hasText(passportStatus.getId(), "passportStatus.id is required; it must not be blank or null");

		final var searchQueryTemplate = "?dateOfBirth={dateOfBirth}&fileNumber={fileNumber}&firstName={firstName}&lastName={lastName}";
		final var searchMethod = methodOn(PassportStatusController.class).search(null, true);
		final var searchLink = linkTo(searchMethod).slash(searchQueryTemplate).withRel("search").expand(passportStatus.getDateOfBirth(), passportStatus.getFileNumber(), passportStatus.getFirstName(), passportStatus.getLastName());

		return createModelWithId(passportStatus.getId(), passportStatus).add(searchLink); // NOSONAR
	}

}