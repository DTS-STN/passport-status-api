package ca.gov.dtsstn.passport.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.ServiceLevelCodeRepository;
import ca.gov.dtsstn.passport.api.service.domain.ServiceLevelCode;
import ca.gov.dtsstn.passport.api.service.domain.mapper.ServiceLevelCodeMapper;

/**
 * Service class to handle {@link ServiceLevelCode} interactions.
 */
@Service
@CacheConfig(cacheNames = { "service-level-codes" })
public class ServiceLevelCodeService {

	private final ServiceLevelCodeMapper mapper;

	private final ServiceLevelCodeRepository repository;

	public ServiceLevelCodeService(@Lazy ServiceLevelCodeMapper mapper, ServiceLevelCodeRepository repository) {
		Assert.notNull(mapper, "mapper is required; it must not be null");
		Assert.notNull(repository, "repository is required; it must not be null");
		this.mapper = mapper;
		this.repository = repository;
	}

	@Cacheable(key = "{ 'id', #id }")
	public Optional<ServiceLevelCode> read(String id) {
		Assert.hasText(id, "id is required; it must not be null or blank");
		return repository.findById(id).map(mapper::fromEntity);
	}

	@Cacheable(key = "{ 'cdoCode', #cdoCode }")
	public Optional<ServiceLevelCode> readByCdoCode(String cdoCode) {
		Assert.notNull(cdoCode, "cdoCode is required; it must not be null");
		return repository.findByCdoCode(cdoCode).map(mapper::fromEntity);
	}

	@Cacheable(key = "{ 'all' }")
	public List<ServiceLevelCode> readAll() {
		return repository.findAll().stream().map(mapper::fromEntity).toList();
	}

	@Cacheable(key = "{ 'isActive', #isActive }")
	public List<ServiceLevelCode> readAllByIsActive(boolean isActive) {
		return repository.findAllByIsActive(isActive).stream().map(mapper::fromEntity).toList();
	}

}
