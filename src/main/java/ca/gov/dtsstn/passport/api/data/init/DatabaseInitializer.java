package ca.gov.dtsstn.passport.api.data.init;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.HttpRequestRepository;
import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument.Status;
import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocumentBuilder;
import net.datafaker.Faker;

/**
 * This class is intended to be used for development purposes only!
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@ConfigurationProperties("application.database-initializer")
public class DatabaseInitializer {

	private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

	private final Faker faker = new Faker(Locale.CANADA_FRENCH, new Random(0L));

	private final Pattern diacriticsPattern	= Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	private final PassportStatusRepository passportStatusRepository;

	private final HttpRequestRepository httpRequestRepository;

	private int duplicateStatusesNumber = 10;

	private int generatedStatusesNumber = 1000;

	public DatabaseInitializer(HttpRequestRepository httpRequestRepository, PassportStatusRepository passportStatusRepository) {
		this.httpRequestRepository = httpRequestRepository;
		this.passportStatusRepository = passportStatusRepository;
	}

	@Async
	@Transactional
	public void initializeData() {
		log.info("Deleting all http traces");
		httpRequestRepository.deleteAll();

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

		final var firstName = faker.name().firstName();
		final var lastName = faker.name().lastName();

		final var applicationRegisterSid = faker.regexify("[A-Z0-9]{8}");
		final var dateOfBirth = faker.date().birthday().toLocalDateTime().toLocalDate().toString();
		final var email = stripDiacritics(firstName + "." + lastName + "@example.com").toLowerCase();
		final var fileNumber = faker.regexify("[A-Z0-9]{8}");
		final var id = faker.random().hex(24);
		final var status = statuses[faker.random().nextInt(statuses.length)];

		return new PassportStatusDocumentBuilder()
			.id(id)
			.applicationRegisterSid(applicationRegisterSid)
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.firstName(firstName)
			.lastName(lastName)
			.status(status)
			.build();
	}

	private PassportStatusDocument generateDuplicatePassportStatus() {
		return new PassportStatusDocumentBuilder()
			.id(faker.random().hex(24))
			.applicationRegisterSid("DUPE0000") // NOSONAR
			.dateOfBirth("2000-01-01")
			.email("DUPE0000@example.com")                       // NOSONAR
			.fileNumber("DUPE0000")                         // NOSONAR
			.firstName("DUPE0000")                           // NOSONAR
			.lastName("DUPE0000")                             // NOSONAR
			.status(Status.APPROVED)
			.build();
	}

	private String stripDiacritics(String string) {
		return diacriticsPattern.matcher(Normalizer.normalize(string, Normalizer.Form.NFD)).replaceAll("");
	}

	public void setGeneratedStatusesNumber(int generatedStatusesNumber) {
		Assert.isTrue(generatedStatusesNumber >= 0, "application.dev.database-initializer.generated-statuses-number must be greater than or equal to zero");
		this.generatedStatusesNumber = generatedStatusesNumber;
	}

	public void setDuplicateStatusesNumber(int duplicateStatusesNumber) {
		Assert.isTrue(duplicateStatusesNumber >= 0, "application.dev.database-initializer.duplicate-statuses-number must be greater than or equal to zero");
		this.duplicateStatusesNumber = duplicateStatusesNumber;
	}

}
