package ca.gov.dtsstn.passport.api.web.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
class ValueOfEnumTests {

	private static Validator validator;

	@BeforeAll
	public static void setupValidatorInstance() {
		validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
	}

	@Test
	void testWhenNull_thenViolationsShouldNotBeReported() {
		// arrange
		final var dto = new MockDTO(null);

		// act
		final var violations = validator.validate(dto);

		// assert
		assertThat(violations).isEmpty();
	}

	@Test
	void testWhenValid_thenViolationsShouldNotBeReported() {
		// arrange
		final var dto = new MockDTO("ONE");

		// act
		final var violations = validator.validate(dto);

		// assert
		assertThat(violations).isEmpty();
	}

	@Test
	void testWhenInvalid_thenViolationsShouldBeReported() {
		// arrange
		final var dto = new MockDTO("TWO");

		// act
		final var violations = validator.validate(dto);

		// assert
		assertThat(violations).hasSize(1)
			.anyMatch(havingPropertyPath("mockEnum")
			.and(havingMessage("must be any of enum class ca.gov.dtsstn.passport.api.web.validation.ValueOfEnumTests$MockEnum")));
	}

	private enum MockEnum { ONE }

	@Valid
	public record MockDTO(@ValueOfEnum(enumClass = MockEnum.class) String mockEnum) {
	}

	public static Predicate<ConstraintViolation<MockDTO>> havingMessage(String message) {
		return l -> message.equals(l.getMessage());
	}

	public static Predicate<ConstraintViolation<MockDTO>> havingPropertyPath(String propertyPath) {
		return l -> propertyPath.equals(l.getPropertyPath().toString());
	}

}
