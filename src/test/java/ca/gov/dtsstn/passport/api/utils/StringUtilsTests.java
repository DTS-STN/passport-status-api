package ca.gov.dtsstn.passport.api.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StringUtilsTests {

	@ParameterizedTest
	@MethodSource
	void testStripNonAlphaNumeric(String input, String expected) {
		final var act = StringUtils.stripNonAlphaNumeric(input);
		assertThat(act).isEqualTo(expected);
	}

	private static Stream<Arguments> testStripNonAlphaNumeric() {
		return Stream.of(
			Arguments.of(null, null),
			Arguments.of("", ""),
			Arguments.of("Control", "Control"),
			Arguments.of("(A)B,.C|d_e1é@!.>", "ABCde1")
		);
	}
}
