package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class JacksonConfig {

	private static final Logger log = LoggerFactory.getLogger(JacksonConfig.class);

	@Bean Module guavaModule() {
		log.info("Creating 'guavaModule' bean");
		return new GuavaModule();
	}

}
