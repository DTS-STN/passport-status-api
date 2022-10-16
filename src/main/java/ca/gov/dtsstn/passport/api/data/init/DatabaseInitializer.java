package ca.gov.dtsstn.passport.api.data.init;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntityBuilder;
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

	private int duplicateStatusesNumber = 10;

	private int generatedStatusesNumber = 1000;

	public DatabaseInitializer(PassportStatusRepository passportStatusRepository) {
		this.passportStatusRepository = passportStatusRepository;
	}

	@Async
	public void initializeData() {
		final var stopWatch = StopWatch.create();

		log.info("Deleting all passport statuses");
		passportStatusRepository.deleteAllInBatch();

		log.info("Generating {} fake random passport statuses", generatedStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var randomPassportStatuses = Stream.generate(this::generateRandomPassportStatus).limit(generatedStatusesNumber).toList();
		partition(randomPassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Fake random data created in {}ms", stopWatch.getTime());

		log.info("Generating {} duplicate fake passport statuses", duplicateStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var duplicatePassportStatuses = Stream.generate(this::generateDuplicatePassportStatus).limit(duplicateStatusesNumber).toList();
		partition(duplicatePassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Duplicate fake data created in {}ms", stopWatch.getTime());

		// TODO :: GjB :: use properties for these
		log.info("Generating passport team fake passport statuses");
		stopWatch.reset(); stopWatch.start();
		passportStatusRepository.save(generatePassportTeamPassportStatus("Greg", "Baker", LocalDate.of(2000, 01, 01), "gregory.j.baker@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Kristopher", "Charbonneau", LocalDate.of(2000, 01, 01), "kristopher.charbonneau@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Maxim", "Lam", LocalDate.of(2000, 01, 01), "maxim.lam@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Sébastien", "Comeau", LocalDate.of(2000, 01, 01), "sebastien.comeau@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Shaun", "Laughland", LocalDate.of(2000, 01, 01), "shaun.laughland@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Stefan", "O'Connell", LocalDate.of(2000, 01, 01), "stefan.oconnell@hrsdc-rhdcc.gc.ca"));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Stéphane", "Viau", LocalDate.of(2000, 01, 01), "stephane.viau@hrsdc-rhdcc.gc.ca"));
		log.info("Passport team fake data created in {}ms", stopWatch.getTime());
	}

	protected <T> List<List<T>> partition(List<T> passportStatuses, int size) {
		final var counter = new AtomicInteger();
		return List.copyOf(passportStatuses.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size)).values());
	}

	protected PassportStatusEntity generateRandomPassportStatus() {
		final var fileNumber = generateFileNumber();
		final var firstName = generateFirstName();
		final var lastName = generateLastName();

		return new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, firstName, lastName))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, firstName, lastName))
			.dateOfBirth(generateDateOfBirth())
			.email(generateEmail(firstName, lastName))
			.fileNumber(fileNumber)
			.firstName(firstName)
			.lastName(lastName)
			.status(generateStatus())
			.build();
	}

	protected PassportStatusEntity generateDuplicatePassportStatus() {
		final var fileNumber = "DUPE0000";
		final var firstName = "DUPE0000";
		final var lastName = "DUPE0000";

		return new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, firstName, lastName))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, firstName, lastName))
			.dateOfBirth(LocalDate.of(2000, 01, 01))
			.email("dupe0000.dupe0000@example.com")
			.fileNumber(fileNumber) // NOSONAR
			.firstName(firstName)   // NOSONAR
			.lastName(lastName)     // NOSONAR
			.status(generateStatus())
			.build();
	}

	protected PassportStatusEntity generatePassportTeamPassportStatus(String firstName, String lastName, LocalDate dateOfBirth, String email) {
		final var fileNumber = generateFileNumber();

		final var passportStatus = new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, firstName, lastName))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, firstName, lastName))
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.firstName(firstName)
			.lastName(lastName)
			.status(generateStatus())
			.build();

		log.trace("Creating fake passport team status: {}", passportStatus);
		return passportStatus;
	}

	protected String generateApplicationRegisterSid(String fileNumber, String firstName, String lastName) {
		return UUID.nameUUIDFromBytes(("applicationRegisterSid" + fileNumber + firstName + lastName).getBytes()).toString();
	}

	protected LocalDate generateDateOfBirth() {
		return faker.date().birthday().toLocalDateTime().toLocalDate();
	}

	protected String generateEmail(final String firstName, final String lastName) {
		return stripDiacritics(firstName + "." + lastName + "@example.com").toLowerCase();
	}

	protected String generateFileNumber() {
		return faker.random().hex(8);
	}

	protected String generateFirstName() {
		return faker.name().firstName();
	}

	protected String generateId(String fileNumber, String firstName, String lastName) {
		return UUID.nameUUIDFromBytes(("id" + fileNumber + firstName + lastName).getBytes()).toString();
	}

	protected String generateLastName() {
		return faker.name().lastName();
	}

	protected ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity.Status generateStatus() {
		final var statuses = PassportStatusEntity.Status.values();
		return statuses[faker.random().nextInt(statuses.length)];
	}

	protected String stripDiacritics(String string) {
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
