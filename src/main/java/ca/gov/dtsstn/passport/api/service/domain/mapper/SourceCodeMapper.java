package ca.gov.dtsstn.passport.api.service.domain.mapper;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.entity.SourceCodeEntity;
import ca.gov.dtsstn.passport.api.service.SourceCodeService;
import ca.gov.dtsstn.passport.api.service.domain.SourceCode;
import jakarta.annotation.PostConstruct;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring")
public abstract class SourceCodeMapper {

	@Autowired
	protected SourceCodeService sourceCodeService;

	@PostConstruct
	public void postConstruct() {
		Assert.notNull(sourceCodeService, "sourceCodeService is required; it must not be null");
	}

	@Nullable
	public SourceCodeEntity fromId(@Nullable String id) {
		return Optional.ofNullable(id)
			.flatMap(sourceCodeService::read)
			.map(this::toEntity)
			.orElse(null);
	}

	@Nullable
	public abstract SourceCode fromEntity(@Nullable SourceCodeEntity sourceCode);

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	public abstract SourceCodeEntity toEntity(@Nullable SourceCode sourceCode);

}
