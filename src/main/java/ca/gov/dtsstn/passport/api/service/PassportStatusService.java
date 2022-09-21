package ca.gov.dtsstn.passport.api.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.mapper.PassportStatusMapper;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
public class PassportStatusService {

	private final PassportStatusMapper passportStatusMapper;

	private final PassportStatusRepository passportStatusRepository;

	public PassportStatusService(PassportStatusMapper passportStatusMapper, PassportStatusRepository passportStatusRepository) {
		Assert.notNull(passportStatusMapper, "passportStatusMapper is required; it must not be null");
		Assert.notNull(passportStatusRepository, "passportStatusRepository is required; it must not be null");
		this.passportStatusMapper = passportStatusMapper;
		this.passportStatusRepository = passportStatusRepository;
	}

	public PassportStatus create(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.isNull(passportStatus.getId(), "passportStatus.id must be null when creating new instance");
		return passportStatusMapper.fromDocument(passportStatusRepository.save(passportStatusMapper.toDocument(passportStatus)));
	}

	public Optional<PassportStatus> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		return passportStatusRepository.findById(id).map(passportStatusMapper::fromDocument);
	}

	@SuppressWarnings({ "java:S4449" })
	public PassportStatus update(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		Assert.notNull(passportStatus.getId(), "passportStatus.id must not be null when updating existing instance");
		final var target = passportStatusRepository.findById(passportStatus.getId()).orElseThrow();
		return passportStatusMapper.fromDocument(passportStatusRepository.save(passportStatusMapper.update(passportStatus, target)));
	}

	public void delete(String id) {
		passportStatusRepository.deleteById(id);
	}

	public Page<PassportStatus> readAll(Pageable pageable) {
		Assert.notNull(pageable, "pageable is required; it must not be null");
		return passportStatusRepository.findAll(pageable).map(passportStatusMapper::fromDocument);
	}

	public Optional<PassportStatus> search(PassportStatus passportStatusProbe) {
		Assert.notNull(passportStatusProbe, "passportStatusProbe is required; it must not be null");
		return passportStatusRepository.findOne(Example.of(passportStatusMapper.toDocument(passportStatusProbe))).map(passportStatusMapper::fromDocument);
	}

}
