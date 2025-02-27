package ca.gov.dtsstn.passport.api.web.validation;

import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.ServiceLevelCodeService;
import ca.gov.dtsstn.passport.api.service.domain.ServiceLevelCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a string is a valid passport status code.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportServiceLevelCodeValidator implements ConstraintValidator<PassportServiceLevelCode, String> {

	private final ServiceLevelCodeService serviceLevelCodeService;

	public PassportServiceLevelCodeValidator(ServiceLevelCodeService serviceLevelCodeService) {
		this.serviceLevelCodeService = serviceLevelCodeService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return serviceLevelCodeService.readByCdoCode(value)
			.map(ServiceLevelCode::getIsActive)
			.filter(Boolean.TRUE::equals)
			.isPresent();
	}

}
