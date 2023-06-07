package ca.gov.dtsstn.passport.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.SourceCodeRepository;
import ca.gov.dtsstn.passport.api.service.domain.SourceCode;
import ca.gov.dtsstn.passport.api.service.domain.mapper.SourceCodeMapper;

/**
 * Service class to handle {@link SourceCode} interactions.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Service
@CacheConfig(cacheNames = { "source-codes" })
public class SourceCodeService {

	private final SourceCodeMapper mapper;

	private final SourceCodeRepository repository;

	public SourceCodeService(@Lazy SourceCodeMapper mapper, SourceCodeRepository repository) {
		Assert.notNull(mapper, "mapper is required; it must not be null");
		Assert.notNull(repository, "repository is required; it must not be null");
		this.mapper = mapper;
		this.repository = repository;
	}

	@Cacheable(key = "{ 'id', #id }")
	public Optional<SourceCode> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		return repository.findById(id).map(mapper::fromEntity);
	}

	@Cacheable(key = "{ 'cdoCode', #cdoCode }")
	public Optional<SourceCode> readByCdoCode(String cdoCode) {
		Assert.notNull(cdoCode, "cdoCode is required; it must not be null");
		return repository.findByCdoCode(cdoCode).map(mapper::fromEntity);
	}

	@Cacheable(key = "{ 'all' }")
	public List<SourceCode> readAll() {
		return repository.findAll().stream().map(mapper::fromEntity).toList();
	}

	@Cacheable(key = "{ 'isActive', #isActive }")
	public List<SourceCode> readAllByIsActive(boolean isActive) {
		return repository.findAllByIsActive(isActive).stream().map(mapper::fromEntity).toList();
	}

}
