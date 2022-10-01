package ca.gov.dtsstn.passport.api.service;

import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.event.ImmutablePassportStatusCreatedEvent;
import ca.gov.dtsstn.passport.api.service.event.ImmutablePassportStatusDeletedEvent;
import ca.gov.dtsstn.passport.api.service.event.ImmutablePassportStatusReadEvent;
import ca.gov.dtsstn.passport.api.service.event.ImmutablePassportStatusUpdatedEvent;
import ca.gov.dtsstn.passport.api.service.mapper.PassportStatusMapper;

/**
 * Service class to handle {@link PassportStatus} interactions.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class PassportStatusService {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final PassportStatusMapper passportStatusMapper = Mappers.getMapper(PassportStatusMapper.class);

	private final PassportStatusRepository passportStatusRepository;

	public PassportStatusService(ApplicationEventPublisher applicationEventPublisher, PassportStatusRepository passportStatusRepository) {
		Assert.notNull(applicationEventPublisher, "applicationEventPublisher is required; it must not be null");
		Assert.notNull(passportStatusRepository, "passportStatusRepository is required; it must not be null");
		this.applicationEventPublisher = applicationEventPublisher;
		this.passportStatusRepository = passportStatusRepository;
	}

	public PassportStatus create(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.isNull(passportStatus.getId(), "passportStatus.id must be null when creating new instance");
		final var createdPassportStatus = passportStatusMapper.fromDocument(passportStatusRepository.save(passportStatusMapper.toDocument(passportStatus)));
		applicationEventPublisher.publishEvent(ImmutablePassportStatusCreatedEvent.of(createdPassportStatus));
		return createdPassportStatus;
	}

	public Optional<PassportStatus> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		final var passportStatus = passportStatusRepository.findById(id).map(passportStatusMapper::fromDocument);
		passportStatus.map(ImmutablePassportStatusReadEvent::of).ifPresent(applicationEventPublisher::publishEvent);
		return passportStatus;
	}

	public PassportStatus update(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.notNull(passportStatus.getId(), "passportStatus.id must not be null when updating existing instance");
		final var originalPassportStatus = passportStatusRepository.findById(passportStatus.getId()).orElseThrow(); // NOSONAR
		final var updatedPassportStatus = passportStatusMapper.fromDocument(passportStatusRepository.save(passportStatusMapper.update(passportStatus, originalPassportStatus)));
		applicationEventPublisher.publishEvent(ImmutablePassportStatusUpdatedEvent.of(passportStatusMapper.fromDocument(originalPassportStatus), updatedPassportStatus));
		return updatedPassportStatus;
	}

	public void delete(String id) {
		passportStatusRepository.findById(id)
			.map(passportStatusMapper::fromDocument)
			.ifPresent(passportStatus -> {
				passportStatusRepository.deleteById(id);
				applicationEventPublisher.publishEvent(ImmutablePassportStatusDeletedEvent.of(passportStatus));
			});
	}

	public Page<PassportStatus> readAll(Pageable pageable) {
		Assert.notNull(pageable, "pageable is required; it must not be null");
		final var passportStatuses = passportStatusRepository.findAll(pageable).map(passportStatusMapper::fromDocument);
		passportStatuses.map(ImmutablePassportStatusReadEvent::of).forEach(applicationEventPublisher::publishEvent);
		return passportStatuses;
	}

	public Page<PassportStatus> search(PassportStatus passportStatusProbe, Pageable pageable) {
		Assert.notNull(passportStatusProbe, "passportStatusProbe is required; it must not be null");
		Assert.notNull(pageable, "pageable is required; it must not be null");
		final var searchablePassportStatusProbe = passportStatusMapper.toSearchableDocument(passportStatusProbe);
		final var passportStatuses = passportStatusRepository.findAllCaseInsensitive(searchablePassportStatusProbe, pageable).map(passportStatusMapper::fromDocument);
		passportStatuses.map(ImmutablePassportStatusReadEvent::of).forEach(applicationEventPublisher::publishEvent);
		return passportStatuses;
	}

}
