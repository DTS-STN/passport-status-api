package ca.gov.dtsstn.passport.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.document.ImmutablePassportStatusDocument;
import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.mapper.PassportStatusMapper;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PassportStatusServiceTests {

	PassportStatusService passportStatusService;

	PassportStatusMapper passportStatusMapper = Mappers.getMapper(PassportStatusMapper.class);

	@Mock PassportStatusRepository passportStatusRepository;

	@BeforeEach void beforeEach() {
		this.passportStatusService = new PassportStatusService(passportStatusMapper, passportStatusRepository);
	}

	@Test void testCreate() {
		when(passportStatusRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		final var passportStatus = passportStatusService.create(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).save(any());
	}

	@Test void testRead() {
		when(passportStatusRepository.findById(any()))
			.thenReturn(Optional.of(ImmutablePassportStatusDocument.builder().build()));

		final var passportStatus = passportStatusService.read("id");

		assertThat(passportStatus).isNotEmpty();
		verify(passportStatusRepository).findById(any());
	}

	@Test void testUpdate() {
		when(passportStatusRepository.findById(any()))
			.thenReturn(Optional.of(ImmutablePassportStatusDocument.builder().build()));
		when(passportStatusRepository.save(any()))
			.thenAnswer(invocation -> invocation.getArgument(0));

		final var passportStatus = passportStatusService.update(ImmutablePassportStatus.builder().id("id").build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).findById(any());
		verify(passportStatusRepository).save(any());
	}

	@Test void testUpdate_notFound() {
		when(passportStatusRepository.findById(any()))
			.thenReturn(Optional.empty());

		final var passportStatus = ImmutablePassportStatus.builder().id("id").build();

		assertThrows(NoSuchElementException.class, () -> {
			passportStatusService.update(passportStatus);
		});
	}

	@Test void testDelete() {
		passportStatusService.delete("id");

		verify(passportStatusRepository).deleteById(any());
	}

	@Test void testReadAll() {
		when(passportStatusRepository.findAll(any(Pageable.class)))
			.thenReturn(Page.empty());

		final var passportStatuses = passportStatusService.readAll(Pageable.unpaged());

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).findAll(any(Pageable.class));
	}

}