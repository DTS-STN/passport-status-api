package ca.gov.dtsstn.passport.api.config.properties;

import javax.jms.Destination;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

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