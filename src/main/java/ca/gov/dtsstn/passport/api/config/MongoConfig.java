package ca.gov.dtsstn.passport.api.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import ca.gov.dtsstn.passport.api.data.converter.DateToLocalDateConverter;
import ca.gov.dtsstn.passport.api.data.converter.LocalDateToDateConverter;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class MongoConfig {

	private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

	@Bean MongoCustomConversions mongoCustomConversions() {
		log.info("Creating 'mongoCustomConversions' bean");
		return new MongoCustomConversions(List.of(new DateToLocalDateConverter(), new LocalDateToDateConverter()));
	}

}
