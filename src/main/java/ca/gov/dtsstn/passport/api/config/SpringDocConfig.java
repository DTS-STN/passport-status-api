package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ca.gov.dtsstn.passport.api.web.model.ImmutablePassportStatusSearchModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String API_KEY_SECURITY = "api-key";

	public static final String BASIC_SECURITY = "basic";

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		final var applicationName = environment.getProperty("spring.application.name", "application");

		return openApi -> {
			log.info("Configuring OpenAPI info");
			openApi.getInfo()
				.title(applicationName)
				.description("This OpenAPI document describes the key areas where developers typically engage with this API.")
				.version(getApplicationVersion(gitProperties));

			log.info("Configuring OpenAPI security schemes");
			openApi.getComponents()
				.addSecuritySchemes(API_KEY_SECURITY, new SecurityScheme().type(Type.APIKEY).in(In.HEADER).name("Authorization"))
				.addSecuritySchemes(BASIC_SECURITY, new SecurityScheme().type(Type.HTTP).scheme("basic")); // NOSONAR

			// Note: this is required for SpringDoc to corrently document PassportStatusSearchModel as a parameter object
			log.info("Configuring SpringDoc parameter objects");
			SpringDocUtils.getConfig().replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
		};
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
