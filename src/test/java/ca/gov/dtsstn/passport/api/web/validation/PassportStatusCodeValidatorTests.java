package ca.gov.dtsstn.passport.api.web.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.ImmutableStatusCode;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PassportStatusCodeValidatorTests {

	PassportStatusCodeValidator sut;

	@Mock
	StatusCodeService statusCodeService;

	@BeforeEach
	void init() {
		sut = new PassportStatusCodeValidator(statusCodeService);
	}

	@Test
	void testIsValid_withNullValue() {
		// arrange & act
		var act = sut.isValid(null, null);

		// assert
		assertThat(act).isTrue();
		verify(statusCodeService, never()).readByCdoCode(any());
	}

	@ParameterizedTest
	@MethodSource
	void testIsValid_WithValue(Optional<StatusCode> readByCdoCodeValue, boolean expected) {
		// arrange
		when(statusCodeService.readByCdoCode(any())).thenReturn(readByCdoCodeValue);

		// act
		var act = sut.isValid("mock-code", null);

		// assert
		assertThat(act).isEqualTo(expected);
		verify(statusCodeService).readByCdoCode(any());
	}

	private static Stream<Arguments> testIsValid_WithValue() {
		return Stream.of(
			Arguments.of(Optional.empty(), false),
			Arguments.of(Optional.ofNullable(ImmutableStatusCode.builder().isActive(null).build()), false),
			Arguments.of(Optional.ofNullable(ImmutableStatusCode.builder().isActive(false).build()), false),
			Arguments.of(Optional.ofNullable(ImmutableStatusCode.builder().isActive(true).build()), true)
		);
	}

}
