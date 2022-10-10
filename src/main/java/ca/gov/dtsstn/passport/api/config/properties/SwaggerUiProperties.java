package ca.gov.dtsstn.passport.api.config.properties;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application.swagger-ui")
public record SwaggerUiProperties(
	@NotBlank String applicationName,
	SwaggerUiProperties.AuthenticationProperties authentication,
	@NotBlank String contactName,
	@NotNull @URL String contactUrl,
	@NotBlank String description,
	List<SwaggerUiProperties.Server> servers,
	@NotNull @URL String tosUrl
) {

	public List<SwaggerUiProperties.Server> servers() {
		return Optional.ofNullable(this.servers).orElse(List.of());
	}

	public record AuthenticationProperties(
		AuthenticationProperties.HttpProperties http,
		AuthenticationProperties.OAuthProperties oauth
	) {

		public record HttpProperties(
			@NotBlank String description
		) {}

		public record OAuthProperties(
			@NotNull @URL String authorizationUrl,
			@NotBlank String description,
			List<OAuthProperties.Scope> scopes,
			@NotNull @URL String tokenUrl
		) {

			public List<OAuthProperties.Scope> scopes() {
				return Optional.ofNullable(this.scopes).orElse(List.of());
			}

			public record Scope(String name, String scope) {}

		}
	}

	public record Server(
		String description,
		@NotNull @URL String url
	) {}
}
