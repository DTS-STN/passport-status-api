package ca.gov.dtsstn.passport.api.config;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ca.gov.dtsstn.passport.api.config.properties.SwaggerUiProperties;
import ca.gov.dtsstn.passport.api.config.properties.SwaggerUiProperties.AuthenticationProperties.OAuthProperties.Scope;
import ca.gov.dtsstn.passport.api.web.model.ImmutablePassportStatusSearchModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String HTTP = "JSON Web Token";

	public static final String OAUTH = "Azure Active Directory";

	static {
		SpringDocUtils.getConfig()
			.replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);
	}

	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties, SwaggerUiProperties swaggerUiProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		return openApi -> {
			openApi.getInfo()
				.title(swaggerUiProperties.applicationName())
				.contact(new Contact()
					.name(swaggerUiProperties.contactName())
					.url(swaggerUiProperties.contactUrl()))
				.description(swaggerUiProperties.description())
				.termsOfService(swaggerUiProperties.tosUrl())
				.version(getApplicationVersion(gitProperties));

			openApi.getComponents()
				.addSecuritySchemes(HTTP, new SecurityScheme()
					.type(Type.HTTP)
					.description(swaggerUiProperties.authentication().http().description())
					.scheme("Bearer")
					.bearerFormat("JWT"));

			openApi.getComponents()
				.addSecuritySchemes(OAUTH, new SecurityScheme()
					.type(Type.OAUTH2)
					.description(swaggerUiProperties.authentication().oauth().description())
					.flows(new OAuthFlows()
						.authorizationCode(new OAuthFlow()
							.authorizationUrl(swaggerUiProperties.authentication().oauth().authorizationUrl())
							.refreshUrl(swaggerUiProperties.authentication().oauth().tokenUrl())
							.scopes(getOAuthScopes(swaggerUiProperties))
							.tokenUrl(swaggerUiProperties.authentication().oauth().tokenUrl()))));

			swaggerUiProperties.servers().stream()
				.map(server -> new Server()
					.description(server.description())
					.url(server.url()))
				.forEach(openApi::addServersItem);
		};
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return String.format("v%s+%s", gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

	protected Scopes getOAuthScopes(SwaggerUiProperties swaggerUiProperties) {
		final var scopes = new Scopes();
		scopes.putAll(swaggerUiProperties.authentication().oauth().scopes().stream().collect(Collectors.toMap(Scope::scope, Scope::name)));
		return scopes;
	}

}
