package ca.gov.dtsstn.passport.api.web.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class GetCertificateApplicationResponseModelAssembler extends AbstractResponseModelAssembler<PassportStatus, GetCertificateApplicationResponseModel> {

	protected final CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	public GetCertificateApplicationResponseModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler) {
		super(PassportStatusController.class, GetCertificateApplicationResponseModel.class, pagedResourcesAssembler);
	}

	@Override
	protected GetCertificateApplicationResponseModel instantiateModel(PassportStatus passportStatus) {
		final var dateOfBirth = passportStatus.getDateOfBirth();
		final var fileNumber = passportStatus.getFileNumber();
		final var firstName = passportStatus.getFirstName();
		final var lastName = passportStatus.getLastName();

		final var searchLink = linkTo(methodOn(PassportStatusController.class).search(dateOfBirth, fileNumber, firstName, lastName, true)).withRel("search");

		return Optional.ofNullable(passportStatus)
			.map(mapper::toModel).orElseThrow()
			.add(searchLink);
	}

}
