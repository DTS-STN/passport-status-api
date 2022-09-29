package ca.gov.dtsstn.passport.api.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepository;
import ca.gov.dtsstn.passport.api.data.ExtendedMongoRepositoryImpl;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackageClasses = { ExtendedMongoRepository.class }, repositoryBaseClass = ExtendedMongoRepositoryImpl.class)
public class DataSourceConfig {

	private final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

	@Bean AuditorAware<String> auditor(Environment environment) {
		log.info("Creating 'auditor' bean");
		final var applicationName = environment.getProperty("spring.application.name", "application");
		return () -> Optional.of(applicationName);
	}

}

