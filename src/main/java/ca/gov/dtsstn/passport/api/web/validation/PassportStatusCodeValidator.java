package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;

/**
 * Checks that a string is a valid passport status code.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusCodeValidator implements ConstraintValidator<PassportStatusCode, String> {

	private final StatusCodeService statusCodeService;

	public PassportStatusCodeValidator(StatusCodeService statusCodeService) {
		this.statusCodeService = statusCodeService;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return statusCodeService.readByCdoCode(value)
			.map(StatusCode::getIsActive)
			.filter(Boolean.TRUE::equals)
			.isPresent();
	}

}
