package ca.gov.dtsstn.passport.api.config.properties;

import java.time.Duration;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application.gcnotify")
public record GcNotifyProperties(
	@NotBlank String englishApiKey,
	@NotBlank String frenchApiKey,
	@NotBlank String baseUrl,
	@Nullable Duration connectTimeout,
	@NestedConfigurationProperty GcNotifyProperties.FileNumberNotificationProperties fileNumberNotification,
	@Nullable Duration readTimeout
) {

	public Duration connectTimeout() {
		return Optional.ofNullable(connectTimeout).orElse(Duration.ofSeconds(30));
	}

	public Duration readTimeout() {
		return Optional.ofNullable(readTimeout).orElse(Duration.ofSeconds(30));
	}

	@Validated
	public record FileNumberNotificationProperties(
		@NotBlank String englishTemplateId,
		@NotBlank String frenchTemplateId
	) {}

}
