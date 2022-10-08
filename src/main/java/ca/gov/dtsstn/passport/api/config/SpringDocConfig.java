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
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String HTTP = "JSON Web Token";

	public static final String OAUTH2 = "Azure Active Directory";

	static {
		SpringDocUtils.getConfig()
			.replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
	}

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		final var applicationName = environment.getProperty("application.swagger.application-name", "application");
		final var authorizationUrl = environment.getProperty("application.authentication.oauth.authorization-uri");
		final var contactName = environment.getProperty("application.swagger.contact-name", "The Development Team");
		final var contactUrl = environment.getProperty("application.swagger.contact-url", "https://canada.ca/");
		final var termsOfServiceUrl = environment.getProperty("application.swagger.terms-of-service-url", "https://canada.ca/");
		final var tokenUrl = environment.getProperty("application.authentication.oauth.token-uri");

		return openApi -> {
			openApi.getInfo()
				.title(applicationName)
				.contact(new Contact().name(contactName).url(contactUrl))
				.description("This OpenAPI document describes the key areas where developers typically engage with this API.")
				.termsOfService(termsOfServiceUrl)
				.version(getApplicationVersion(gitProperties));

			openApi.getComponents()
				.addSecuritySchemes(HTTP, new SecurityScheme()
					.type(Type.HTTP)
					.description("Use the JSON Web Token authorization for service account access.")
					.scheme("Bearer")
					.bearerFormat("JWT"));

			openApi.getComponents()
				.addSecuritySchemes(OAUTH2, new SecurityScheme()
					.type(Type.OAUTH2)
					.description("Use the Azure Active Directory authorization for Government of Canada employ access.")
					.flows(new OAuthFlows()
						.authorizationCode(new OAuthFlow()
							.authorizationUrl(authorizationUrl)
							.refreshUrl(tokenUrl)
							.tokenUrl(tokenUrl))));
		};
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
