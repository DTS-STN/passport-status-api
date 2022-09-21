package ca.gov.dtsstn.passport.api.web.assembler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.mapper.PassportStatusModelMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PassportStatusModelAssemblerTests {

	PassportStatusModelAssembler passportStatusModelAssembler;

	PassportStatusModelMapper passportStatusModelMapper = Mappers.getMapper(PassportStatusModelMapper.class);

	@Mock PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler;

	@BeforeEach void beforeEach() {
		this.passportStatusModelAssembler = new PassportStatusModelAssembler(pagedResourcesAssembler, passportStatusModelMapper);
	}

	@Test void testInstantiateModel() {
		final var passportStatus = passportStatusModelAssembler.instantiateModel(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
	}

	@Test void testToModel() {
		final var passportStatus = passportStatusModelAssembler.toModel(ImmutablePassportStatus.builder().id("id").build());

		assertThat(passportStatus).isNotNull();
	}

	@Test void testToPagedModel() {
		when(pagedResourcesAssembler.toModel(ArgumentMatchers.<Page<PassportStatus>> any(), ArgumentMatchers.<RepresentationModelAssembler<PassportStatus, PassportStatusModel>> any()))
			.thenReturn(PagedModel.empty());

		passportStatusModelAssembler.toPagedModel(Page.empty());

		verify(pagedResourcesAssembler).toModel(ArgumentMatchers.<Page<PassportStatus>> any(), ArgumentMatchers.<RepresentationModelAssembler<PassportStatus, PassportStatusModel>> any());
	}

}
