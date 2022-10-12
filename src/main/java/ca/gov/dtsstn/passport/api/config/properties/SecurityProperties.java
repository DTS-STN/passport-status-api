package ca.gov.dtsstn.passport.api.config.properties;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application.security")
public record SecurityProperties(
	SecurityProperties.ContentSecurityPolicyProperties contentSecurityPolicy,
	SecurityProperties.CorsProperties cors,
	SecurityProperties.OAuthProperties oauth
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
					Optional.ofNullable(defaultSrc).map(value -> String.format("default-src %s;", value)),
					Optional.ofNullable(childSrc).map(value -> String.format("child-src %s;", value)),
					Optional.ofNullable(connectSrc).map(value -> String.format("connect-src %s;", value)),
					Optional.ofNullable(fontSrc).map(value -> String.format("font-src %s;", value)),
					Optional.ofNullable(frameSrc).map(value -> String.format("frame-src %s;", value)),
					Optional.ofNullable(imgSrc).map(value -> String.format("img-src %s;", value)),
					Optional.ofNullable(manifestSrc).map(value -> String.format("manifest-src %s;", value)),
					Optional.ofNullable(mediaSrc).map(value -> String.format("media-src %s;", value)),
					Optional.ofNullable(objectSrc).map(value -> String.format("object-src %s;", value)),
					Optional.ofNullable(prefetchSrc).map(value -> String.format("prefetch-src %s;", value)),
					Optional.ofNullable(scriptSrc).map(value -> String.format("script-src %s;", value)),
					Optional.ofNullable(scriptSrcElem).map(value -> String.format("script-src-elem %s;", value)),
					Optional.ofNullable(scriptSrcAttr).map(value -> String.format("script-src-attr %s;", value)),
					Optional.ofNullable(styleSrc).map(value -> String.format("style-src %s;", value)),
					Optional.ofNullable(styleSrcElem).map(value -> String.format("style-src-elem %s;", value)),
					Optional.ofNullable(styleSrcAttr).map(value -> String.format("style-src-attr %s;", value)),
					Optional.ofNullable(workerSrc).map(value -> String.format("worker-src %s;", value)))
				.filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.joining(" "));
		}

	}

	public record CorsProperties(
		@Nullable List<String> allowedHeaders,
		@Nullable List<String> allowedMethods,
		@Nullable List<String> allowedOrigins,
		@Nullable List<String> exposedHeaders
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
