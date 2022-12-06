package ca.gov.dtsstn.passport.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application")
@EnableConfigurationProperties({
	GcNotifyProperties.class,
	JmsProperties.class,
	SecurityProperties.class,
	SwaggerUiProperties.class
})
public record ApplicationProperties(
	@NestedConfigurationProperty GcNotifyProperties gcnotify,
	@NestedConfigurationProperty JmsProperties jms,
	@NestedConfigurationProperty SecurityProperties security,
	@NestedConfigurationProperty SwaggerUiProperties swaggerUi
) {}
