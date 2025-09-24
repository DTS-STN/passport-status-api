package ca.gov.dtsstn.passport.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntityBuilder;
import ca.gov.dtsstn.passport.api.event.PassportStatusCreateConflictEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusDeletedEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusReadEvent;
import ca.gov.dtsstn.passport.api.event.PassportStatusUpdatedEvent;
import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.mapper.PassportStatusMapper;

/**
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

	@Test void testCreate_whenNoConflict() {
		when(passportStatusRepository.findByApplicationRegisterSidAndVersion(any(), any())).thenReturn(Optional.empty());
		when(passportStatusRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());
		when(passportStatusMapper.toEntity(any())).thenReturn(new PassportStatusEntityBuilder().build());

		final var passportStatus = passportStatusService.create(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).findByApplicationRegisterSidAndVersion(any(), any());
		verify(passportStatusRepository).save(any());
		verify(passportStatusMapper).fromEntity(any());
		verify(passportStatusMapper).toEntity(any());
		verify(applicationEventPublisher, never()).publishEvent(any(PassportStatusCreateConflictEvent.class));
		verify(applicationEventPublisher).publishEvent(any(PassportStatusCreatedEvent.class));
	}

	@Test void testCreate_whenConflict() {
		when(passportStatusRepository.findByApplicationRegisterSidAndVersion(any(), any())).thenReturn(Optional.of(new PassportStatusEntity()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatus = passportStatusService.create(ImmutablePassportStatus.builder().build());

		assertThat(passportStatus).isNotNull();
		verify(passportStatusRepository).findByApplicationRegisterSidAndVersion(any(), any());
		verify(passportStatusRepository, never()).save(any());
		verify(passportStatusMapper).fromEntity(any());
		verify(passportStatusMapper, never()).toEntity(any());
		verify(applicationEventPublisher).publishEvent(any(PassportStatusCreateConflictEvent.class));
		verify(applicationEventPublisher, never()).publishEvent(any(PassportStatusCreatedEvent.class));
	}

	@Test void testRead() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.of(new PassportStatusEntityBuilder().build()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatus = passportStatusService.read("id");

		assertThat(passportStatus).isNotEmpty();
		verify(passportStatusRepository).findById(any());
		verify(passportStatusMapper).fromEntity(any());
		verify(applicationEventPublisher).publishEvent(any(PassportStatusReadEvent.class));
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
		verify(applicationEventPublisher).publishEvent(any(PassportStatusUpdatedEvent.class));
	}

	@Test void testUpdate_notFound() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.empty());

		final var passportStatus = ImmutablePassportStatus.builder().id("id").build();

		assertThrows(NoSuchElementException.class, () -> passportStatusService.update(passportStatus));
		verify(passportStatusRepository).findById(any());
		verify(passportStatusRepository, never()).save(any());
		verify(passportStatusMapper, never()).fromEntity(any());
		verify(applicationEventPublisher, never()).publishEvent(any(PassportStatusUpdatedEvent.class));
	}

	@Test void testDelete() {
		when(passportStatusRepository.findById(any())).thenReturn(Optional.of(new PassportStatusEntityBuilder().build()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		passportStatusService.delete("id");

		verify(passportStatusRepository).deleteById(any());
		verify(passportStatusMapper).fromEntity(any());
		verify(applicationEventPublisher).publishEvent(any(PassportStatusDeletedEvent.class));
	}

	@Test void testReadAll() {
		final var pageMock = new PageImpl<PassportStatusEntity>(List.of(new PassportStatusEntity(), new PassportStatusEntity()));
		when(passportStatusRepository.findAll(pageMock.getPageable())).thenReturn(pageMock);
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatuses = passportStatusService.readAll(pageMock.getPageable());

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).findAll(any(Pageable.class));
		verify(passportStatusMapper, times(2)).fromEntity(any());
		verify(applicationEventPublisher, times(2)).publishEvent(any(PassportStatusReadEvent.class));
	}

	@Test void testSearch() {
		when(passportStatusRepository.fileNumberSearch(any(), any(), any(), any())).thenReturn(List.of(new PassportStatusEntity()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatuses = passportStatusService.fileNumberSearch(LocalDate.now(), "fileNumber", "givenName", "surname");

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).fileNumberSearch(any(), any(), any(), any());
		verify(passportStatusMapper).fromEntity(any());
		verify(applicationEventPublisher).publishEvent(any(PassportStatusReadEvent.class));
	}

		@Test void testSearchSingleName() {
		when(passportStatusRepository.fileNumberSearchSingleName(any(), any(), any())).thenReturn(List.of(new PassportStatusEntity()));
		when(passportStatusMapper.fromEntity(any())).thenReturn(ImmutablePassportStatus.builder().build());

		final var passportStatuses = passportStatusService.fileNumberSearchSingleName(LocalDate.now(), "fileNumber", "singleName");

		assertThat(passportStatuses).isNotNull();
		verify(passportStatusRepository).fileNumberSearchSingleName(any(), any(), any());
		verify(passportStatusMapper).fromEntity(any());
		verify(applicationEventPublisher).publishEvent(any(PassportStatusReadEvent.class));
	}
}
