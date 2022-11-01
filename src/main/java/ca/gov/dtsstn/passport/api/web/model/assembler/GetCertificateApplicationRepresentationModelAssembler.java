package ca.gov.dtsstn.passport.api.web.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationRepresentationModel;
import ca.gov.dtsstn.passport.api.web.model.mapper.CertificateApplicationModelMapper;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class GetCertificateApplicationRepresentationModelAssembler extends AbstractResponseModelAssembler<PassportStatus, GetCertificateApplicationRepresentationModel> {

	protected final CertificateApplicationModelMapper mapper;

	public GetCertificateApplicationRepresentationModelAssembler(PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler, CertificateApplicationModelMapper mapper) {
		super(PassportStatusController.class, GetCertificateApplicationRepresentationModel.class, pagedResourcesAssembler);
		this.mapper = mapper;
	}

	@Override
	protected GetCertificateApplicationRepresentationModel instantiateModel(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");

		final var dateOfBirth = passportStatus.getDateOfBirth();
		final var fileNumber = passportStatus.getFileNumber();
		final var givenName = passportStatus.getGivenName();
		final var lastName = passportStatus.getLastName();

		final var searchLink = linkTo(methodOn(PassportStatusController.class).search(dateOfBirth, fileNumber, givenName, lastName, true)).withRel("search");

		return Optional.ofNullable(passportStatus)
			.map(mapper::toModel).orElseThrow()
			.add(searchLink);
	}

}
