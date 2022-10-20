package ca.gov.dtsstn.passport.api.service;

import java.util.concurrent.atomic.AtomicReference;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.config.properties.JmsProperties;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.exception.PassportStatusJmsException;

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

	public String send(PassportStatus passportStatus) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");

		final var message = new AtomicReference<Message>();
		final var destination = jmsProperties.destination().passportStatusDestination();
		final MessagePostProcessor postProcessor = messagePostProcessor -> {
			message.set(messagePostProcessor);
			return messagePostProcessor;
		};

		try {
			jmsTemplate.convertAndSend(destination, passportStatus, postProcessor);
			final var messageId = message.get().getJMSMessageID();
			log.debug("Sending passport status to {}; MessageId='{}'", destination, messageId);
			return messageId;
		} catch (JMSException jmsException) {
			final var errorMessage = String.format("Fails to send passport status message to %s", destination);
			throw new PassportStatusJmsException(errorMessage, jmsException);
		}
	}

	@JmsListener(destination = "${application.jms.destination.passport-status}")
	public void receive(PassportStatus passportStatus,
			@Header(JmsHeaders.MESSAGE_ID) String messageId,
			@Header(JmsHeaders.DESTINATION) String destination) {
		Assert.notNull(passportStatus, "passportStatus is required; it must not be null");
		log.debug("Receive passport status from {}; MessageId='{}';", destination, messageId);
		passportStatusService.create(passportStatus);
	}

}
