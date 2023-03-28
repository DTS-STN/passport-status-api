package ca.gov.dtsstn.passport.api.web.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks that a string is a valid ISO 8601 compliant date.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class DateValidator implements ConstraintValidator<Date, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }

		try {
			return LocalDate.parse(value) != null;
		}
		catch (final DateTimeParseException ex) {
			return false;
		}
	}

}
