package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class JmsConfig {

	private static final Logger log = LoggerFactory.getLogger(JmsConfig.class);

	@Bean MessageConverter jacksonJmsMessageConverter() {
		log.info("Creating 'jacksonJmsMessageConverter' bean");

		final var objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		final var converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		converter.setObjectMapper(objectMapper);
		return converter;
	}

}
