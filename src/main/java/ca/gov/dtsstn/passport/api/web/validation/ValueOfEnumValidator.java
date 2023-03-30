package ca.gov.dtsstn.passport.api.web.validation;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

	private List<String> values;

	@Override
	public void initialize(ValueOfEnum enumValidator) {
		values = Stream.of(enumValidator.enumClass().getEnumConstants())
				.map(Enum::name)
				.toList();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) { return true; }
		return values.contains(value);
	}
}