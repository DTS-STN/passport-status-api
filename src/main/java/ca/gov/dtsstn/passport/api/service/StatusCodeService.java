package ca.gov.dtsstn.passport.api.service;

import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.StatusCodeRepository;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;
import ca.gov.dtsstn.passport.api.service.domain.mapper.StatusCodeMapper;

/**
 * Service class to handle {@link StatusCode} interactions.
 *
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Service
public class StatusCodeService {

	private final StatusCodeMapper mapper = Mappers.getMapper(StatusCodeMapper.class);

	private final StatusCodeRepository repository;

	public StatusCodeService(StatusCodeRepository repository) {
		Assert.notNull(repository, "repository is required; it must not be null");
		this.repository = repository;
	}

	public Optional<StatusCode> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		return repository.findById(id).map(mapper::fromEntity);
	}

	public Optional<StatusCode> readByCdoCode(String cdoCode) {
		Assert.notNull(cdoCode, "cdoCode is required; it must not be null");
		return repository.findByCdoCode(cdoCode).map(mapper::fromEntity);
	}

	public List<StatusCode> readAll() {
		return repository.findAll().stream().map(mapper::fromEntity).toList();
	}

	public List<StatusCode> readAllByIsActive(boolean isActive) {
		return repository.findAllByIsActive(isActive).stream().map(mapper::fromEntity).toList();
	}
}
