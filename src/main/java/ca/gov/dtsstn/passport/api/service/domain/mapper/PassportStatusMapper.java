package ca.gov.dtsstn.passport.api.service.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * MapStruct mapper that maps {@link PassportStatus} instances to {@link PassportStatusDocument} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusMapper {

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	PassportStatusEntity toEntity(@Nullable PassportStatus passportStatus);

	@Nullable
	PassportStatus fromEntity(@Nullable PassportStatusEntity passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "isNew", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	PassportStatusEntity update(@Nullable PassportStatus passportStatus, @MappingTarget PassportStatusEntity target);

}
