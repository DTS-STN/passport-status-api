package ca.gov.dtsstn.passport.api.service.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface StatusCodeMapper {

	@Nullable
	StatusCode fromEntity(@Nullable StatusCodeEntity passportStatus);

	@Nullable
	@Mapping(target = "isNew", ignore = true)
	StatusCodeEntity toEntity(@Nullable StatusCode passportStatus);

}
