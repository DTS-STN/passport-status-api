package ca.gov.dtsstn.passport.api.web.mapper;

import org.mapstruct.Mapper;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusModelMapper {

	PassportStatus toDomain(PassportStatusModel passportStatus);

	PassportStatusModel fromDomain(PassportStatus passportStatus);

}
