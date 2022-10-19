package ca.gov.dtsstn.passport.api.web.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusReadResponseModelMapper {

	PassportStatus toDomain(PassportStatusReadResponseModel passportStatusReadResponse);

	@Mapping(target = "add", ignore = true) // fixes a weird vscode/eclipse & mapstruct bug quirk/bug 💩
	PassportStatusReadResponseModel fromDomain(PassportStatus passportStatus);

}
