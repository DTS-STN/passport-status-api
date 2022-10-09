package ca.gov.dtsstn.passport.api.config.properties;

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
	@NotNull @URL String tosUrl
) {

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
			@NotNull @URL String tokenUrl
		) {}
	}

}
