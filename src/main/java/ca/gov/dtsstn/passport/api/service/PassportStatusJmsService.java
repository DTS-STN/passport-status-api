package ca.gov.dtsstn.passport.api.service;

import java.util.concurrent.atomic.AtomicReference;

import javax.jms.JMSException;
import javax.jms.Message;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.config.properties.JmsProperties;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatusMapper;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusCreateRequestModel;

/**
 * @author Sébastien Comeau (sebastien.comeau@hrsdc-rhdcc.gc.ca)
 */
@Component
public class PassportStatusJmsService {

	private static final Logger log = LoggerFactory.getLogger(PassportStatusJmsService.class);

	private final JmsProperties jmsProperties;

	private final JmsTemplate jmsTemplate;

	private final PassportStatusMapper passportStatusMapper = Mappers.getMapper(PassportStatusMapper.class);

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

	public void send(PassportStatusCreateRequestModel passportStatusCreateRequestModel) {
		Assert.notNull(passportStatusCreateRequestModel, "passportStatusCreateRequestModel is required; it must not be null");
		final var message = new AtomicReference<Message>();
		final var destination = jmsProperties.destination().passportStatusDestination();

		jmsTemplate.convertAndSend(destination, passportStatusCreateRequestModel,
			messagePostProcessor -> {
				message.set(messagePostProcessor);
				return messagePostProcessor;
			});

		try {
			final var messageId = message.get().getJMSMessageID();
			log.debug("Sending passport status to {}; MessageId='{}';", destination, messageId);
		} catch (JMSException jmsException) {
			log.error("Fails to get the message ID while sending passport status to {};", destination, jmsException);
		}
	}

	@JmsListener(destination = "${application.jms.destination.passport-status}")
	public void receive(PassportStatusCreateRequestModel passportStatusCreateRequestModel,
			@Header(JmsHeaders.MESSAGE_ID) String messageId,
			@Header(JmsHeaders.DESTINATION) String destination) {
		Assert.notNull(passportStatusCreateRequestModel, "passportStatusCreateRequestModel is required; it must not be null");
		log.debug("Receive passport status from {}; MessageId='{}';", destination, messageId);
		passportStatusService.create(passportStatusMapper.fromCreateRequestModel(passportStatusCreateRequestModel));
	}

}
