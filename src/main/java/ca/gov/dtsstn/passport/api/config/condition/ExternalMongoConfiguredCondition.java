package ca.gov.dtsstn.passport.api.config.condition;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public class ExternalMongoConfiguredCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		final var uri = context.getEnvironment().getProperty("spring.data.mongodb.uri");
		final var host = context.getEnvironment().getProperty("spring.data.mongodb.host");
		final var port = context.getEnvironment().getProperty("spring.data.mongodb.port");
		return Stream.of(uri, host, port).anyMatch(Objects::nonNull);
	}

}
