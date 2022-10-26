package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

/**
 * Checks that a string represents a boolean value.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class BooleanValidator implements ConstraintValidator<Boolean, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return "true".equals(value) || "false".equals(value);
	}

}
