package ca.gov.dtsstn.passport.api.web.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class GetCertificateApplicationRepresentationModelAssembler extends AbstractResponseModelAssembler<PassportStatus, GetCertificateApplicationRepresentationModel> {

	protected final CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	public GetCertificateApplicationRepresentationModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler) {
		super(PassportStatusController.class, GetCertificateApplicationRepresentationModel.class, pagedResourcesAssembler);
	}

	@Override
	protected GetCertificateApplicationRepresentationModel instantiateModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");

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
