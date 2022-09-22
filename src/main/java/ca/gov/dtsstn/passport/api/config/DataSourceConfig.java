package ca.gov.dtsstn.passport.api.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableMongoAuditing
@SuppressWarnings({ "java:S3305" })
public class DataSourceConfig {

	private final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired Environment environment;

	@Bean AuditorAware<String> auditor() {
		log.info("Creating 'auditor' bean");
		final var applicationName = environment.getProperty("spring.application.name", "application");
		return () -> Optional.of(applicationName);
	}

}
