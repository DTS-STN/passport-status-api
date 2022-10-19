package ca.gov.dtsstn.passport.api.web.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.mapstruct.factory.Mappers;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusReadResponseModelAssembler extends AbstractResponseModelAssembler<PassportStatus, PassportStatusReadResponseModel> {

	protected final PassportStatusReadResponseModelMapper mapper = Mappers.getMapper(PassportStatusReadResponseModelMapper.class);

	public PassportStatusReadResponseModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler) {
		super(PassportStatusController.class, PassportStatusReadResponseModel.class, pagedResourcesAssembler);
	}

	@Override
	protected PassportStatusReadResponseModel instantiateModel(PassportStatus passportStatus) {
		final var dateOfBirth = passportStatus.getDateOfBirth();
		final var fileNumber = passportStatus.getFileNumber();
		final var firstName = passportStatus.getFirstName();
		final var lastName = passportStatus.getLastName();

		final var searchLink = linkTo(methodOn(PassportStatusController.class).search(dateOfBirth, fileNumber, firstName, lastName, true)).withRel("search");

		return mapper.fromDomain(passportStatus).add(searchLink);
	}

}
