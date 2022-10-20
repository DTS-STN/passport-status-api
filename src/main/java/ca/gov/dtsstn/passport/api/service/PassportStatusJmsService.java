package ca.gov.dtsstn.passport.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.config.properties.JmsProperties;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusJmsService {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusJmsService.class);

	private final JmsProperties jmsProperties;

	private final JmsTemplate jmsTemplate;

	private final PassportStatusService passportStatusService;

	public PassportStatusJmsService(JmsProperties jmsProperties,
			JmsTemplate jmsTemplate,
			PassportStatusService passportStatusService) {
		Assert.notNull(jmsProperties, "jmsProperties is required; it must not be null");
		Assert.notNull(jmsTemplate, "jmsTemplate is required; it must not be null");
		Assert.notNull(passportStatusService, "passportStatusService is required; it must not be null");
		this.jmsProperties = jmsProperties;
		this.jmsTemplate = jmsTemplate;
		this.passportStatusService = passportStatusService;
	}

	public void send(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		final var destination = jmsProperties.destination().passportStatusDestination();
		log.debug("Sending passport status to {}; {}'", destination, passportStatus);
		jmsTemplate.convertAndSend(destination, passportStatus);
	}

	@JmsListener(destination = "${application.jms.destination.passport-status}")
	public void receive(PassportStatus passportStatus,
			@Header(JmsHeaders.MESSAGE_ID) String messageId,
			@Header(JmsHeaders.DESTINATION) String destination) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		log.debug("Receive passport status from {}; MessageId='{}', {}", destination, messageId, passportStatus);
		passportStatusService.create(passportStatus);
	}

}
