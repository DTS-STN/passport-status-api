package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Embedded MongoDB configuration...
 * <p>
 * To enable embedded MongoDB, you must run the application using the `embedded-mongo` Spring profile.
 * This can be done using the following configuration in your `application-local.yaml` file:
 * <p>
 * <pre>
 * spring:
 *   profiles:
 *	   active:
 *	     - embedded-mongo
 * </pre>
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
@EnableAutoConfiguration(exclude = { EmbeddedMongoAutoConfiguration.class })
@Profile({ "!embedded-mongo & !test" }) // don't disable embedded mongodb if these profiles are active
public class EmbeddedMongoConfig {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedMongoConfig.class);

	public EmbeddedMongoConfig() { // NOSONAR
		log.info("Disabling embedded MongoDB because the following profiles are not active: [embedded-mongo, test]");
	}

}
