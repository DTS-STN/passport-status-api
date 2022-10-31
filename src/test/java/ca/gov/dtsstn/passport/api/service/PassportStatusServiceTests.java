package ca.gov.dtsstn.passport.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntityBuilder;
import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.mapper.PassportStatusMapper;

/**
 * TODO :: GjB :: verify event publisher is fired for each method
 * TODO :: GjB :: add test for queueCreation
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PassportStatusServiceTests {

	PassportStatusService passportStatusService;

	@Mock ApplicationEventPublisher applicationEventPublisher;

	@Mock PassportStatusRepository passportStatusRepository;

	@Mock PassportStatusMapper passportStatusMapper;

	@BeforeEach void beforeEach() {
		this.passportStatusService = new PassportStatusService(applicationEventPublisher, passportStatusMapper, passportStatusRepository);
	}

	@Test void testCreate() {
		when(passportStatusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());
		when(passportStatusMapper.toEntity(any())).thenReturn(new PassportStatusEntityBuilder().build());

		final var passportStatus = passportStatusService.create(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).save(any());
		verify(passportStatusMapper).fromEntity(any());
		verify(passportStatusMapper).toEntity(any());
	}

	@Test void testRead() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.of(new PassportStatusEntityBuilder().build()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatus = passportStatusService.read("id");

		assertThat(passportStatus).isNotEmpty();
		verify(passportStatusRepository).findById(any());
		verify(passportStatusMapper).fromEntity(any());
	}

	@Test void testUpdate() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.of(new PassportStatusEntityBuilder().build()));
		when(passportStatusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatus = passportStatusService.update(ImmutablePassportStatus.builder().id("id").build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).findById(any());
		verify(passportStatusRepository).save(any());
		verify(passportStatusMapper, times(2)).fromEntity(any());
	}

	@Test void testUpdate_notFound() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.empty());

		final var passportStatus = ImmutablePassportStatus.builder().id("id").build();

		assertThrows(NoSuchElementException.class, () -> passportStatusService.update(passportStatus));
	}

	@Test void testDelete() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.of(new PassportStatusEntityBuilder().build()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		passportStatusService.delete("id");

		verify(passportStatusRepository).deleteById(any());
		verify(passportStatusMapper).fromEntity(any());
	}

	@Test void testReadAll() {
		when(passportStatusRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

		final var passportStatuses = passportStatusService.readAll(Pageable.unpaged());

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).findAll(any(Pageable.class));
	}

	@Test void testSearch() {
		when(passportStatusRepository.fileNumberSearch(any(), any(), any(), any())).thenReturn(List.of());

		final var passportStatuses = passportStatusService.fileNumberSearch(LocalDate.now(), "fileNumber", "firstName", "lastName");

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).fileNumberSearch(any(), any(), any(), any());
	}
}
