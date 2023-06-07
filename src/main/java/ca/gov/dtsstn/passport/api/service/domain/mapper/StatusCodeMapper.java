package ca.gov.dtsstn.passport.api.service.domain.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;
import jakarta.annotation.PostConstruct;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring")
public abstract class StatusCodeMapper {

	@Autowired
	protected StatusCodeService statusCodeService;

	@PostConstruct
	public void postConstruct() {
		Assert.notNull(statusCodeService, "statusCodeService is required; it must not be null");
	}

	@Nullable
	public StatusCodeEntity fromId(@Nullable String id) {
		return Optional.ofNullable(id)
			.flatMap(statusCodeService::read)
			.map(this::toEntity)
			.orElse(null);
	}

	@Nullable
	public abstract StatusCode fromEntity(@Nullable StatusCodeEntity statusCode);

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	public abstract StatusCodeEntity toEntity(@Nullable StatusCode statusCode);

}
