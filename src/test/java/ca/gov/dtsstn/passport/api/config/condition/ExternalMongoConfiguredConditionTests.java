package ca.gov.dtsstn.passport.api.config.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class ExternalMongoConfiguredConditionTests {

	ExternalMongoConfiguredCondition externalMongoConfiguredCondition;

	@BeforeEach void beforeEach() {
		this.externalMongoConfiguredCondition = new ExternalMongoConfiguredCondition();
	}

	@Test void testMatches_false() {
		final var environment = mock(Environment.class);
		final var context = mock(ConditionContext.class);
		final var metadata = mock(AnnotatedTypeMetadata.class);

		when(context.getEnvironment()).thenReturn(environment);

		var matches = externalMongoConfiguredCondition.matches(context, metadata);

		assertThat(matches).isFalse();
	}

	@Test void testMatches_true() {
		final var environment = mock(Environment.class);
		final var context = mock(ConditionContext.class);

		when(context.getEnvironment()).thenReturn(environment);
		when(environment.getProperty(any())).thenReturn("mongodb://localhost/");

		var matches = externalMongoConfiguredCondition.matches(context, mock(AnnotatedTypeMetadata.class));

		assertThat(matches).isTrue();
	}

}
