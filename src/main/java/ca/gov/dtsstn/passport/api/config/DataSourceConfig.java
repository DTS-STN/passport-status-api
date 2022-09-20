package ca.gov.dtsstn.passport.api.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableMongoAuditing
public class DataSourceConfig {

	@Bean AuditorAware<String> jpaAuditor() {
		return () -> Optional.of("passport-status-api");
	}

}
