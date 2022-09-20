package ca.gov.dtsstn.passport.api.dev;

import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.document.ImmutablePassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;
import net.datafaker.Faker;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class DataInitializer implements ApplicationListener<ApplicationStartedEvent> {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	final Faker faker = new Faker(new Random(0L));

	final PassportStatusRepository passportStatusRepository;

	public DataInitializer(PassportStatusRepository passportStatusRepository) {
		this.passportStatusRepository = passportStatusRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("Generating fake data...");

		final var stopWatch = StopWatch.createStarted();
		passportStatusRepository.deleteAll();
		Stream.generate(this::generatepassportStatus).limit(1000).forEach(passportStatusRepository::save);
		log.info("Fake data created in {}ms", stopWatch.getTime());
	}

	private PassportStatusDocument generatepassportStatus() {
		return ImmutablePassportStatusDocument.builder()
			.id(faker.regexify("[a-f0-9]{24}"))
			.esrf(faker.regexify("[A-Za-z]{8}"))
			.firstName(faker.name().firstName())
			.lastName(faker.name().lastName())
			.dateOfBirth(faker.date().birthday().toLocalDateTime().toLocalDate())
			.email(faker.internet().emailAddress())
			.build();
	}

}
