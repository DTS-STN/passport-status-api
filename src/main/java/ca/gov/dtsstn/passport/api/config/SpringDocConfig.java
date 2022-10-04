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
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String API_KEY_SECURITY = "api-key";

	public static final String BASIC_SECURITY = "basic";

	static {
		SpringDocUtils.getConfig()
			.replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
	}

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		final var applicationName = environment.getProperty("application.swagger.application-name", "application");
		final var contactName = environment.getProperty("application.swagger.contact-name", "The Development Team");
		final var contactUrl = environment.getProperty("application.swagger.contact-url", "https://canada.ca/");
		final var termsOfServiceUrl = environment.getProperty("application.swagger.terms-of-service-url", "https://canada.ca/");

		return openApi -> {
			openApi.getInfo()
				.title(applicationName)
				.contact(new Contact().name(contactName).url(contactUrl))
				.description("This OpenAPI document describes the key areas where developers typically engage with this API.")
				.termsOfService(termsOfServiceUrl)
				.version(getApplicationVersion(gitProperties));

			openApi.getComponents()
				// .addSecuritySchemes(API_KEY_SECURITY, new SecurityScheme().type(Type.APIKEY).in(In.HEADER).name("Authorization"))
				.addSecuritySchemes(BASIC_SECURITY, new SecurityScheme().type(Type.HTTP).scheme("basic")); // NOSONAR
		};
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
