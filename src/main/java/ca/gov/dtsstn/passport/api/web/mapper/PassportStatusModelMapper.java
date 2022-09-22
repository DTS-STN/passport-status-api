package ca.gov.dtsstn.passport.api.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

/**
 * MapStruct mapper that maps {@link PassportStatusModel} instances to {@link PassportStatus} instances (and vice versa).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusModelMapper {

	PassportStatus toDomain(PassportStatusModel passportStatus);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "version", ignore = true)
	PassportStatus toDomain(PassportStatusSearchModel passportStatusSearch);

	PassportStatusModel fromDomain(PassportStatus passportStatus);

}
