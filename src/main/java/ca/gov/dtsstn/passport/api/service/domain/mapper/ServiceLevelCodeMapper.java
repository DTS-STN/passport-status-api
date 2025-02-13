package ca.gov.dtsstn.passport.api.service.domain.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.entity.ServiceLevelCodeEntity;
import ca.gov.dtsstn.passport.api.service.ServiceLevelCodeService;
import ca.gov.dtsstn.passport.api.service.domain.ServiceLevelCode;
import jakarta.annotation.PostConstruct;

@Mapper(componentModel = "spring")
public abstract class ServiceLevelCodeMapper {

	@Autowired
	protected ServiceLevelCodeService serviceLevelCodeService;

	@PostConstruct
	public void postConstruct() {
		Assert.notNull(serviceLevelCodeService, "serviceLevelCodeService is required; it must not be null");
	}

	@Nullable
	public ServiceLevelCodeEntity fromId(@Nullable String id) {
		return Optional.ofNullable(id)
			.flatMap(serviceLevelCodeService::read)
			.map(this::toEntity)
			.orElse(null);
	}

	@Nullable
	public abstract ServiceLevelCode fromEntity(@Nullable ServiceLevelCodeEntity serviceLevelCode);

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	public abstract ServiceLevelCodeEntity toEntity(@Nullable ServiceLevelCode serviceLevelCode);

}
