package ca.gov.dtsstn.passport.api.web.model;

import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
@Style(validationMethod = ValidationMethod.NONE)
public interface ElectronicServiceCreateRequestModelMapper {

	@Mapping(target = "applicationRegisterSid", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "fileNumber", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "status", ignore = true)
	PassportStatus toDomain(ElectronicServiceCreateRequestModel model);

}
