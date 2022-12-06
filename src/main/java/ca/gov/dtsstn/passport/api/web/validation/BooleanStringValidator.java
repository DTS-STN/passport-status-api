package ca.gov.dtsstn.passport.api.web.validation;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a string represents a boolean value.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class BooleanStringValidator implements ConstraintValidator<BooleanString, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return "true".equals(value) || "false".equals(value);
	}

}
