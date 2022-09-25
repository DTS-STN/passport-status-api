package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import ca.gov.dtsstn.passport.api.web.model.ImmutablePassportStatusSearchModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	@Bean ApplicationListener<ContextRefreshedEvent> springDocCustomizer() {
		log.info("Creating 'springDocCustomizer' bean");

		return args -> {
			log.info("Configuring SpringDoc parameter objects");
			SpringDocUtils.getConfig().replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
		};
	}

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		final var applicationName = environment.getProperty("spring.application.name", "application");

		return openApi -> openApi
			.info(new Info()
				.title(applicationName)
				.description("This OpenAPI document describes the key areas where developers typically engage with this API.")
				.version(getApplicationVersion(gitProperties)));
	}

	private String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
