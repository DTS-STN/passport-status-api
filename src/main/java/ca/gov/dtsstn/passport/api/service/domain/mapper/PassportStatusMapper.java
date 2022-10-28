package ca.gov.dtsstn.passport.api.service.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * MapStruct mapper that maps {@link PassportStatus} instances to {@link PassportStatusDocument} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring")
public abstract class PassportStatusMapper {

	@Autowired
	protected StatusCodeMapper statusCodeMapper;

	@Autowired
	protected StatusCodeService statusCodeService;

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "statusCode", source = "passportStatus", qualifiedByName = { "toStatusCodeEntity" })
	public abstract PassportStatusEntity toEntity(@Nullable PassportStatus passportStatus);

	@Nullable
	@Mapping(target = "statusCodeId", source = "statusCode.id")
	public abstract PassportStatus fromEntity(@Nullable PassportStatusEntity passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "isNew", ignore = true)
	@Mapping(target = "statusCode", source = "passportStatus", qualifiedByName = { "toStatusCodeEntity" })
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	public abstract PassportStatusEntity update(@Nullable PassportStatus passportStatus, @MappingTarget PassportStatusEntity target);

	@Named("toStatusCodeEntity")
	protected StatusCodeEntity toStatusCodeEntity(PassportStatus passportStatus) {
		return statusCodeMapper.toEntity(statusCodeService.read(passportStatus.getStatusCodeId()).orElse(null));
	}

}
