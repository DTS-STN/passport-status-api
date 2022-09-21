package ca.gov.dtsstn.passport.api.dev;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.document.ImmutablePassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument.Status;
import net.datafaker.Faker;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@ConfigurationProperties("application.dev.data-initializer")
public class DataInitializer implements ApplicationListener<ApplicationStartedEvent> {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	private final Faker faker = new Faker(new Random(0L));

	private final PassportStatusRepository passportStatusRepository;

	private int generatedStatusesNumber = 1000;

	public DataInitializer(PassportStatusRepository passportStatusRepository) {
		this.passportStatusRepository = passportStatusRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("Generating {} fake random passport statuses...", generatedStatusesNumber);
		final var stopWatch = StopWatch.createStarted();
		passportStatusRepository.deleteAll();
		Stream.generate(this::generateRandomPassportStatus).limit(generatedStatusesNumber).forEach(passportStatusRepository::save);
		log.info("Fake random data created in {}ms", stopWatch.getTime());

		log.info("Generating 10 duplicate fake passport statuses...");
		Stream.generate(this::generateDuplicatePassportStatus).limit(10).forEach(passportStatusRepository::save);
		log.info("Duplicate fake data created in {}ms", stopWatch.getTime());
	}

	private PassportStatusDocument generateRandomPassportStatus() {
		final var statuses = PassportStatusDocument.Status.values();
		return ImmutablePassportStatusDocument.builder()
			.fileNumber(faker.regexify("[A-Z0-9]{8}"))
			.firstName(faker.name().firstName())
			.lastName(faker.name().lastName())
			.dateOfBirth(faker.date().birthday().toLocalDateTime().toLocalDate())
			.status(statuses[faker.random().nextInt(statuses.length)])
			.build();
	}

	@SuppressWarnings({ "java:S1192" })
	private PassportStatusDocument generateDuplicatePassportStatus() {
		return ImmutablePassportStatusDocument.builder()
			.fileNumber("DUPE0000")
			.firstName("DUPE0000")
			.lastName("DUPE0000")
			.dateOfBirth(LocalDate.of(2000, 01, 01))
			.status(Status.ACCEPTED)
			.build();
	}

	public void setGeneratedStatusesNumber(int generatedStatusesNumber) {
		Assert.isTrue(generatedStatusesNumber >= 0, "application.dev.data-initializer.generated-statuses-number must be greater than or equal to zero");
		this.generatedStatusesNumber = generatedStatusesNumber;
	}

}
