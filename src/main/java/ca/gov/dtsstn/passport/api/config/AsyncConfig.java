package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@EnableAsync
@Configuration
public class AsyncConfig {

	private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

	public AsyncConfig() { // NOSONAR
		log.info("Enabling async processing");
	}

}
