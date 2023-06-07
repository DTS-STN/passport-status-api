package ca.gov.dtsstn.passport.api.service.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.SourceCodeEntity;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * MapStruct mapper that maps {@link PassportStatus} instances to {@link PassportStatusEntity} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring", uses = { StatusCodeMapper.class })
public interface PassportStatusMapper {

	/**
	 * ID of the IRIS source system; see: src/main/resources/db-migrations/common/v3-[common]-add-source-code-table.sql
	 *
	 * TODO :: GjB :: remove in future commit
	 */
	static final String IRIS_SOURCE_ID = "327c25eb-e3f4-492e-bd47-4feb20189e78";

	/**
	 * Temporary method that will generate a SourceCodeEntity that maps to IRIS.
	 *
	 * TODO :: GjB :: remove this and create an actual mapper in a future commit
	 */
	default SourceCodeEntity irisSourceCode() {
		final var sourceCode = new SourceCodeEntity();
		sourceCode.setId(IRIS_SOURCE_ID);
		return sourceCode;
	}

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "sourceCode", expression = "java(irisSourceCode())") // TODO :: GjB :: remove in future commit
	@Mapping(target = "statusCode", source = "statusCodeId")
	PassportStatusEntity toEntity(@Nullable PassportStatus passportStatus);

	@Nullable
	@Mapping(target = "statusCodeId", source = "statusCode.id")
	PassportStatus fromEntity(@Nullable PassportStatusEntity passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "sourceCode", expression = "java(irisSourceCode())") // TODO :: GjB :: remove in future commit
	@Mapping(target = "statusCode", source = "statusCodeId")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	PassportStatusEntity update(@Nullable PassportStatus passportStatus, @MappingTarget PassportStatusEntity target);

}
