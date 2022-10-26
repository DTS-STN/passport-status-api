package ca.gov.dtsstn.passport.api.web.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.PastOrPresent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Checks that a string represents a date in the past or present.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PastOrPresentValidator implements ConstraintValidator<PastOrPresent, String> {

	private static final Logger log = LoggerFactory.getLogger(PastOrPresentValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }

		try {
			final var now = LocalDate.now();
			final var localDate = LocalDate.parse(value);
 			return localDate.isBefore(now) || localDate.equals(now);
		}
		catch (final DateTimeParseException ex) {
			log.debug("Value is not an ISO 8601 compliant date: {}", value);
			return false;
		}
	}

}
