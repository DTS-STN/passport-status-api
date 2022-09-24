package ca.gov.dtsstn.passport.api.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import ca.gov.dtsstn.passport.api.config.condition.ExternalMongoConfiguredCondition;
import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepository;
import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepositoryImpl;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableMongoAuditing
@SuppressWarnings({ "java:S3305" })
@EnableMongoRepositories(basePackageClasses = { ExtendedMongoRepository.class }, repositoryBaseClass = ExtendedMongoRepositoryImpl.class)
public class DataSourceConfig {

	private final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired Environment environment;

	@Bean AuditorAware<String> auditor() {
		log.info("Creating 'auditor' bean");
		final var applicationName = environment.getProperty("spring.application.name", "application");
		return () -> Optional.of(applicationName);
	}

	@Configuration
	@Profile("!test")
	@Conditional({ ExternalMongoConfiguredCondition.class })
	@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
	class EmbeddedMongoConfiguration {

		@Bean ApplicationListener<ContextRefreshedEvent> embeddedMongoConfigurationNotifier() {
			return args -> log.info("External MongoDB server configured; disabling embedded Mongo...");
		}

	}

}

