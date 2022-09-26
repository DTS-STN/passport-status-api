package ca.gov.dtsstn.passport.api.web.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PagedResourcesAssembler;

import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PassportStatusModelAssemblerTests {

	PassportStatusModelAssembler passportStatusModelAssembler;

	@Mock PagedResourcesAssembler<PassportStatus> pagedResourcesAssembler;

	@BeforeEach void beforeEach() {
		this.passportStatusModelAssembler = new PassportStatusModelAssembler(pagedResourcesAssembler);
	}

	@Test void testInstantiateModel() {
		final var passportStatus = passportStatusModelAssembler.instantiateModel(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
	}

	@Test void testToModel() {
		final var passportStatus = passportStatusModelAssembler.toModel(ImmutablePassportStatus.builder().id("id").build());

		assertThat(passportStatus).isNotNull();
	}

}
