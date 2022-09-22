package ca.gov.dtsstn.passport.api.config;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepository;
import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepositoryImpl;
import ca.gov.dtsstn.passport.api.data.converter.DateToLocalDateConverter;
import ca.gov.dtsstn.passport.api.data.converter.LocalDateToDateConverter;

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

	@Autowired DateToLocalDateConverter dateToLocalDateConverter;

	@Autowired LocalDateToDateConverter localDateToDateConverter;

	@Bean AuditorAware<String> auditor() {
		log.info("Creating 'auditor' bean");
		final var applicationName = environment.getProperty("spring.application.name", "application");
		return () -> Optional.of(applicationName);
	}

	@Bean MongoCustomConversions mongoCustomConversions() {
		log.info("Creating 'mongoCustomConversions' bean");
		return new MongoCustomConversions(List.of(dateToLocalDateConverter, localDateToDateConverter));
	}

	@Configuration
	@Profile("!test")
	@ConditionalOnProperty("spring.data.mongodb.uri")
	@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
	class EmbeddedMongoConfiguration {

		@Bean ApplicationListener<ContextRefreshedEvent> embeddedMongoConfigurationNotifier() {
			return args -> log.info("Property spring.data.mongodb.uri has a value; disabling embedded Mongo...");
		}

	}

}
