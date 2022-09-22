package ca.gov.dtsstn.passport.api.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * MapStruct mapper that maps {@link PassportStatus} instances to {@link PassportStatusDocument} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusMapper {

	PassportStatusDocument toDocument(PassportStatus passportStatus);

	PassportStatus fromDocument(PassportStatusDocument passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "version", ignore = true)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	PassportStatusDocument update(PassportStatus passportStatus, @MappingTarget PassportStatusDocument target);

}
