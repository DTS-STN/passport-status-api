package ca.gov.dtsstn.passport.api.dev;

import java.util.Locale;
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

import ca.gov.dtsstn.passport.api.data.HttpTraceRepository;
import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.document.ImmutablePassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument.Status;
import net.datafaker.Faker;

/**
 * This class is intended to be used for development purposes only!
 * <p>
 * DataInitializer is a Spring {@link ApplicationStartedEvent} listener that
 * initializes a development database to a baseline configuration (using datafaker).
 * <p>
 * XXX :: GjB :: this component should only fire when intended; current it is confired to always fire, which WILL DESTROY DATA!!
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@ConfigurationProperties("application.dev.data-initializer")
public class DataInitializer implements ApplicationListener<ApplicationStartedEvent> {

	private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

	private final Faker faker = new Faker(Locale.CANADA_FRENCH, new Random(0L));

	private final PassportStatusRepository passportStatusRepository;

	private final HttpTraceRepository httpTraceRepository;

	private int duplicateStatusesNumber = 10;

	private int generatedStatusesNumber = 1000;

	public DataInitializer(HttpTraceRepository httpTraceRepository, PassportStatusRepository passportStatusRepository) {
		this.httpTraceRepository = httpTraceRepository;
		this.passportStatusRepository = passportStatusRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartedEvent event) {
		log.info("Deleting all http traces");
		httpTraceRepository.deleteAll();

		log.info("Deleting all passport statuses");
		passportStatusRepository.deleteAll();

		log.info("Generating {} fake random passport statuses", generatedStatusesNumber);
		final var stopWatch = StopWatch.createStarted();
		Stream.generate(this::generateRandomPassportStatus).limit(generatedStatusesNumber).forEach(passportStatusRepository::save);
		log.info("Fake random data created in {}ms", stopWatch.getTime());

		log.info("Generating {} duplicate fake passport statuses", duplicateStatusesNumber);
		Stream.generate(this::generateDuplicatePassportStatus).limit(duplicateStatusesNumber).forEach(passportStatusRepository::save);
		log.info("Duplicate fake data created in {}ms", stopWatch.getTime());
	}

	protected PassportStatusDocument generateRandomPassportStatus() {
		final var statuses = PassportStatusDocument.Status.values();
		return ImmutablePassportStatusDocument.builder()
			.id(faker.random().hex(24))
			.fileNumber(faker.regexify("[A-Z0-9]{8}"))
			.firstName(faker.name().firstName())
			.lastName(faker.name().lastName())
			.dateOfBirth(faker.date().birthday().toLocalDateTime().toLocalDate().toString())
			.status(statuses[faker.random().nextInt(statuses.length)])
			.build();
	}

	@SuppressWarnings({ "java:S1192" })
	private PassportStatusDocument generateDuplicatePassportStatus() {
		return ImmutablePassportStatusDocument.builder()
			.id(faker.random().hex(24))
			.fileNumber("DUPE0000")
			.firstName("DUPE0000")
			.lastName("DUPE0000")
			.dateOfBirth("2000-01-01")
			.status(Status.ACCEPTED)
			.build();
	}

	public void setGeneratedStatusesNumber(int generatedStatusesNumber) {
		Assert.isTrue(generatedStatusesNumber >= 0, "application.dev.data-initializer.generated-statuses-number must be greater than or equal to zero");
		this.generatedStatusesNumber = generatedStatusesNumber;
	}

	public void setDuplicateStatusesNumber(int duplicateStatusesNumber) {
		Assert.isTrue(duplicateStatusesNumber >= 0, "application.dev.data-initializer.duplicate-statuses-number must be greater than or equal to zero");
		this.duplicateStatusesNumber = duplicateStatusesNumber;
	}

}
