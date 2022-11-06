package ca.gov.dtsstn.passport.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusDeletedEvent;
import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusReadEvent;
import ca.gov.dtsstn.passport.api.event.ImmutablePassportStatusUpdatedEvent;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.mapper.PassportStatusMapper;

/**
 * Service class to handle {@link PassportStatus} interactions.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class PassportStatusService {

	private final ApplicationEventPublisher eventPublisher;

	private final PassportStatusMapper mapper;

	private final PassportStatusRepository repository;

	public PassportStatusService(ApplicationEventPublisher eventPublisher, PassportStatusMapper mapper, PassportStatusRepository repository) {
		Assert.notNull(eventPublisher, "eventPublisher is required; it must not be null");
		Assert.notNull(mapper, "mapper is required; it must not be null");
		Assert.notNull(repository, "repository is required; it must not be null");
		this.eventPublisher = eventPublisher;
		this.mapper = mapper;
		this.repository = repository;
	}

	public PassportStatus create(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.isNull(passportStatus.getId(), "passportStatus.id must be null when creating new instance");
		final var createdPassportStatus = mapper.fromEntity(repository.save(mapper.toEntity(passportStatus))); // NOSONAR (nullable param)
		eventPublisher.publishEvent(ImmutablePassportStatusCreatedEvent.of(createdPassportStatus));
		return createdPassportStatus;
	}

	public Optional<PassportStatus> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		final var passportStatus = repository.findById(id).map(mapper::fromEntity);
		passportStatus.map(ImmutablePassportStatusReadEvent::of).ifPresent(eventPublisher::publishEvent);
		return passportStatus;
	}

	public PassportStatus update(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.notNull(passportStatus.getId(), "passportStatus.id must not be null when updating existing instance");
		final var originalPassportStatus = repository.findById(passportStatus.getId()).orElseThrow(); // NOSONAR (nullable param)
		final var updatedPassportStatus = mapper.fromEntity(repository.save(mapper.update(passportStatus, originalPassportStatus)));
		eventPublisher.publishEvent(ImmutablePassportStatusUpdatedEvent.of(mapper.fromEntity(originalPassportStatus), updatedPassportStatus));
		return updatedPassportStatus;
	}

	public void delete(String id) {
		repository.findById(id)
			.map(mapper::fromEntity)
			.ifPresent(passportStatus -> {
				repository.deleteById(id);
				eventPublisher.publishEvent(ImmutablePassportStatusDeletedEvent.of(passportStatus));
			});
	}

	public Page<PassportStatus> readAll(Pageable pageable) {
		Assert.notNull(pageable, "pageable is required; it must not be null");
		final var passportStatuses = repository.findAll(pageable).map(mapper::fromEntity);
		passportStatuses.map(ImmutablePassportStatusReadEvent::of).forEach(eventPublisher::publishEvent);
		return passportStatuses;
	}

	public List<PassportStatus> applicationRegisterSidSearch(String applicationRegisterSid) {
		Assert.hasText(applicationRegisterSid, "applicationRegisterSid is required; it must not be null or blank");
		final var passportStatuses = repository.findAllByApplicationRegisterSid(applicationRegisterSid).stream().map(mapper::fromEntity).toList();
		passportStatuses.stream().map(ImmutablePassportStatusReadEvent::of).forEach(eventPublisher::publishEvent);
		return passportStatuses;
	}

	public List<PassportStatus> emailSearch(LocalDate dateOfBirth, String email, String givenName, String surname) {
		Assert.notNull(dateOfBirth, "dateOfBirthis required; it must not be null");
		Assert.hasText(email, "email is required; it must not be blank or null");
		Assert.hasText(givenName, "givenName is required, it must not be blank or null");
		Assert.hasText(surname, "surname is required; it must not be blank or null");
		final var passportStatuses = repository.emailSearch(email, dateOfBirth, givenName, surname).stream().map(mapper::fromEntity).toList();
		passportStatuses.stream().map(ImmutablePassportStatusReadEvent::of).forEach(eventPublisher::publishEvent);
		return passportStatuses;
	}

	public List<PassportStatus> fileNumberSearch(LocalDate dateOfBirth, String fileNumber, String givenName, String surname) {
		Assert.notNull(dateOfBirth, "dateOfBirthis required; it must not be null");
		Assert.hasText(fileNumber, "fileNumber is required; it must not be blank or null");
		Assert.hasText(givenName, "givenName is required, it must not be blank or null");
		Assert.hasText(surname, "surname is required; it must not be blank or null");
		final var passportStatuses = repository.fileNumberSearch(fileNumber, dateOfBirth, givenName, surname).stream().map(mapper::fromEntity).toList();
		passportStatuses.stream().map(ImmutablePassportStatusReadEvent::of).forEach(eventPublisher::publishEvent);
		return passportStatuses;
	}

}
