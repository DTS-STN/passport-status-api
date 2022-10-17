package ca.gov.dtsstn.passport.api.web.model;

import org.mapstruct.Mapper;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface PassportStatusModelMapper {

	@Nullable
	PassportStatus toDomain(@Nullable PassportStatusModel passportStatus);

	@Nullable
	PassportStatusModel fromDomain(@Nullable PassportStatus passportStatus);

}
