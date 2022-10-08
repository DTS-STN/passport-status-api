package ca.gov.dtsstn.passport.api.config;

import java.util.List;

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
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String OAUTH2_SECURITY = "oauth2";

	static {
		SpringDocUtils.getConfig()
			.replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
	}

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		final var applicationName = environment.getProperty("application.swagger.application-name", "application");
		final var authorizationUrl = environment.getProperty("application.authentication.oauth.authorization-uri");
		final var authScopes = List.of(environment.getProperty("application.authentication.oauth.auth-scopes", String[].class));
		final var contactName = environment.getProperty("application.swagger.contact-name", "The Development Team");
		final var contactUrl = environment.getProperty("application.swagger.contact-url", "https://canada.ca/");
		final var termsOfServiceUrl = environment.getProperty("application.swagger.terms-of-service-url", "https://canada.ca/");
		final var tokenUrl = environment.getProperty("application.authentication.oauth.token-uri");

		final var scopes = new Scopes();
		authScopes.forEach(scope -> scopes.addString(scope, scope));

		return openApi -> {
			openApi.getInfo()
				.title(applicationName)
				.contact(new Contact().name(contactName).url(contactUrl))
				.description("This OpenAPI document describes the key areas where developers typically engage with this API.")
				.termsOfService(termsOfServiceUrl)
				.version(getApplicationVersion(gitProperties));

			openApi.getComponents()
				.addSecuritySchemes(OAUTH2_SECURITY, new SecurityScheme()
					.type(Type.OAUTH2)
					.flows(new OAuthFlows()
						.implicit(new OAuthFlow()
							.authorizationUrl(authorizationUrl)
							.scopes(scopes)
							.tokenUrl(tokenUrl))));
		};
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
