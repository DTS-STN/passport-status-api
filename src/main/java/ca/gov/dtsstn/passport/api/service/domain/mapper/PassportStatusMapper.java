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
 * MapStruct mapper that maps {@link PassportStatus} instances to {@link PassportStatusEntity} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring", uses = { SourceCodeMapper.class, StatusCodeMapper.class, DeliveryMethodCodeMapper.class, ServiceLevelCodeMapper.class})
public interface PassportStatusMapper {

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "sourceCode", source = "sourceCodeId")
	@Mapping(target = "statusCode", source = "statusCodeId")
  @Mapping(target = "deliveryMethodCode", source = "deliveryMethodCodeId")
  @Mapping(target = "serviceLevelCode", source = "serviceLevelCodeId")
	PassportStatusEntity toEntity(@Nullable PassportStatus passportStatus);

	@Nullable
	@Mapping(target = "sourceCodeId", source = "sourceCode.id")
	@Mapping(target = "statusCodeId", source = "statusCode.id")
	@Mapping(target = "deliveryMethodCodeId", source = "deliveryMethodCode.id")
	@Mapping(target = "serviceLevelCodeId", source = "serviceLevelCode.id")
	PassportStatus fromEntity(@Nullable PassportStatusEntity passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "sourceCode", source = "sourceCodeId")
	@Mapping(target = "statusCode", source = "statusCodeId")
  @Mapping(target = "deliveryMethodCode", source = "deliveryMethodCodeId")
  @Mapping(target = "serviceLevelCode", source = "serviceLevelCodeId")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	PassportStatusEntity update(@Nullable PassportStatus passportStatus, @MappingTarget PassportStatusEntity target);

}
