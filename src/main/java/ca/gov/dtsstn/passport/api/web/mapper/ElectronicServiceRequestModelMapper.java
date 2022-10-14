package ca.gov.dtsstn.passport.api.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.ElectronicServiceRequestModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface ElectronicServiceRequestModelMapper {

	@Mapping(target = "applicationRegisterSid", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "fileNumber", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "status", ignore = true)
	PassportStatus toDomain(ElectronicServiceRequestModel electronicServiceRequest);

}
