package ca.gov.dtsstn.passport.api.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import ca.gov.dtsstn.passport.api.data.init.DatabaseInitializer;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableJpaAuditing
public class DataSourceConfig {

	private final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	@Bean AuditorAware<String> auditor(Environment environment) {
		log.info("Creating 'auditor' bean");
		final var applicationName = environment.getProperty("spring.application.name", "application");
		return () -> Optional.of(applicationName);
	}

	@ConditionalOnProperty({ "application.database-initializer.run-on-startup" })
	@Bean ApplicationListener<ApplicationStartedEvent> databaseInitializerStartupListener(DatabaseInitializer databaseInitializer) {
		log.info("Creating 'databaseInitializerStartupListener' bean");
		return event -> {
			log.info("Initializing data");
			databaseInitializer.initializeData();
		};
	}

}

