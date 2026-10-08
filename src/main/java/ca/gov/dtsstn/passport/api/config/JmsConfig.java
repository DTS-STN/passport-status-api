package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import tools.jackson.databind.json.JsonMapper;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class JmsConfig {

	private static final Logger log = LoggerFactory.getLogger(JmsConfig.class);

	@Bean MessageConverter jacksonJmsMessageConverter(JsonMapper jsonMapper) {
		log.info("Creating 'jacksonJmsMessageConverter' bean");

		final var converter = new JacksonJsonMessageConverter(jsonMapper);
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
