package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import ca.gov.dtsstn.passport.api.web.model.ImmutablePassportStatusSearchModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	@Bean ApplicationListener<ContextRefreshedEvent> springDocCustomizer() {
		return args -> {
			log.info("Configuring SpringDoc parameter objects...");
			SpringDocUtils.getConfig().replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
		};
	}

}
