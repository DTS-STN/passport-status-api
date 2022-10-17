package ca.gov.dtsstn.passport.api.config.properties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application.security")
public record SecurityProperties(
	@NestedConfigurationProperty SecurityProperties.ContentSecurityPolicyProperties contentSecurityPolicy,
	@NestedConfigurationProperty SecurityProperties.CorsProperties cors,
	@NestedConfigurationProperty SecurityProperties.OAuthProperties oauth
) {

	/**
	 * @see https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy
	 */
	public record ContentSecurityPolicyProperties(
		@Nullable String childSrc,
		@Nullable String connectSrc,
		@Nullable String defaultSrc,
		@Nullable String fontSrc,
		@Nullable String frameSrc,
		@Nullable String imgSrc,
		@Nullable String manifestSrc,
		@Nullable String mediaSrc,
		@Nullable String objectSrc,
		@Nullable String prefetchSrc,
		@Nullable String scriptSrc,
		@Nullable String scriptSrcElem,
		@Nullable String scriptSrcAttr,
		@Nullable String styleSrc,
		@Nullable String styleSrcElem,
		@Nullable String styleSrcAttr,
		@Nullable String workerSrc
	) {

		public String toString() {
			return Stream.of(
					Optional.ofNullable(defaultSrc).map("default-src %s;"::formatted),
					Optional.ofNullable(childSrc).map("child-src %s;"::formatted),
					Optional.ofNullable(connectSrc).map("connect-src %s;"::formatted),
					Optional.ofNullable(fontSrc).map("font-src %s;"::formatted),
					Optional.ofNullable(frameSrc).map("frame-src %s;"::formatted),
					Optional.ofNullable(imgSrc).map("img-src %s;"::formatted),
					Optional.ofNullable(manifestSrc).map("manifest-src %s;"::formatted),
					Optional.ofNullable(mediaSrc).map("media-src %s;"::formatted),
					Optional.ofNullable(objectSrc).map("object-src %s;"::formatted),
					Optional.ofNullable(prefetchSrc).map("prefetch-src %s;"::formatted),
					Optional.ofNullable(scriptSrc).map("script-src %s;"::formatted),
					Optional.ofNullable(scriptSrcElem).map("script-src-elem %s;"::formatted),
					Optional.ofNullable(scriptSrcAttr).map("script-src-attr %s;"::formatted),
					Optional.ofNullable(styleSrc).map("style-src %s;"::formatted),
					Optional.ofNullable(styleSrcElem).map("style-src-elem %s;"::formatted),
					Optional.ofNullable(styleSrcAttr).map("style-src-attr %s;"::formatted),
					Optional.ofNullable(workerSrc).map("worker-src %s;"::formatted))
				.filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.joining(" "));
		}

	}

	public record CorsProperties(
		List<String> allowedHeaders,
		List<String> allowedMethods,
		List<String> allowedOrigins,
		List<String> exposedHeaders
	) {

		public List<String> allowedHeaders() {
			return Optional.ofNullable(this.allowedHeaders).orElse(List.of());
		}

		public List<String> allowedMethods() {
			return Optional.ofNullable(this.allowedMethods).orElse(List.of());
		}

		public List<String> allowedOrigins() {
			return Optional.ofNullable(this.allowedOrigins).orElse(List.of());
		}

		public List<String> exposedHeaders() {
			return Optional.ofNullable(this.exposedHeaders).orElse(List.of());
		}

	}

	public record OAuthProperties(
		@Nullable String authScopes,
		@NotNull @URL String authorizationUrl,
		@NotBlank String clientId,
		@NotNull @URL String discoveryUrl,
		@NotBlank String tenantId,
		@NotNull @URL String tokenUrl
	) {}

}
