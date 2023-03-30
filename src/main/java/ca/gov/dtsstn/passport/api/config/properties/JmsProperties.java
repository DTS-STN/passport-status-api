package ca.gov.dtsstn.passport.api.config.properties;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.jms.Destination;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Validated
@ConfigurationProperties("application.jms")
public record JmsProperties(
	@NotNull DestinationProperties destination
) {

	public record DestinationProperties(
		@NotBlank String passportStatus
	) {

		public Destination passportStatusDestination() {
			return new ActiveMQQueue(passportStatus);
		}

	}

}