package ca.gov.dtsstn.passport.api.data.init;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.StopWatch;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntityBuilder;
import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.mapper.StatusCodeMapper;
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

	private final StatusCodeService statusCodeService;

	private final StatusCodeMapper statusCodeMapper = Mappers.getMapper(StatusCodeMapper.class);

	private int duplicateStatusesNumber = 10;

	private int generatedStatusesNumber = 1000;

	public DatabaseInitializer(PassportStatusRepository passportStatusRepository, StatusCodeService statusCodeService) {
		Assert.notNull(passportStatusRepository, "passportStatusRepository is required; it must not be null");
		Assert.notNull(statusCodeService, "statusCodeService is required; it must not be null");
		this.passportStatusRepository = passportStatusRepository;
		this.statusCodeService = statusCodeService;
	}

	@Async
	public void initializeData() {
		final var stopWatch = StopWatch.create();

		log.info("Deleting all passport statuses");
		passportStatusRepository.deleteAllInBatch();

		log.info("Fetch all active passport status codes");
		final var statusCodes = statusCodeService.readAllByIsActive(true).stream().map(statusCodeMapper::toEntity).toList();

		log.info("Generating {} fake random passport statuses", generatedStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var randomPassportStatuses = Stream.generate(() -> generateRandomPassportStatus(statusCodes)).limit(generatedStatusesNumber).toList();
		partition(randomPassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Fake random data created in {}ms", stopWatch.getTime());

		log.info("Generating {} duplicate fake passport statuses", duplicateStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var duplicatePassportStatuses = Stream.generate(() -> generateDuplicatePassportStatus(statusCodes)).limit(duplicateStatusesNumber).toList();
		partition(duplicatePassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Duplicate fake data created in {}ms", stopWatch.getTime());

		// TODO :: GjB :: use properties for these
		log.info("Generating passport team fake passport statuses");
		stopWatch.reset(); stopWatch.start();
		passportStatusRepository.save(generatePassportTeamPassportStatus("Greg", "Baker", LocalDate.of(2000, 01, 01), "gregory.j.baker@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Kristopher", "Charbonneau", LocalDate.of(2000, 01, 01), "kristopher.charbonneau@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Maxim", "Lam", LocalDate.of(2000, 01, 01), "maxim.lam@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Sébastien", "Comeau", LocalDate.of(1985, 01, 10), "sebastien.comeau@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Shaun", "Laughland", LocalDate.of(2000, 01, 01), "shaun.laughland@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Stefan", "O'Connell", LocalDate.of(2000, 01, 01), "stefan.oconnell@hrsdc-rhdcc.gc.ca", statusCodes));
		passportStatusRepository.save(generatePassportTeamPassportStatus("Stéphane", "Viau", LocalDate.of(2000, 01, 01), "stephane.viau@hrsdc-rhdcc.gc.ca", statusCodes));
		log.info("Passport team fake data created in {}ms", stopWatch.getTime());
	}

	protected <T> List<List<T>> partition(List<T> passportStatuses, int size) {
		final var counter = new AtomicInteger();
		return List.copyOf(passportStatuses.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size)).values());
	}

	protected PassportStatusEntity generateRandomPassportStatus(List<StatusCodeEntity> statusCodes) {
		final var fileNumber = generateFileNumber();
		final var givenName = generateGivenName();
		final var surname = generateSurname();

		return new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, givenName, surname))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(generateDateOfBirth())
			.email(generateEmail(givenName, surname))
			.fileNumber(fileNumber)
			.givenName(givenName)
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
			.statusDate(generateStatusDate(LocalDate.of(2000, 01, 01), LocalDate.now()))
			.build();
	}

	protected PassportStatusEntity generateDuplicatePassportStatus(List<StatusCodeEntity> statusCodes) {
		final var dupeString = "DUPE0000";
		final var fileNumber = dupeString;
		final var givenName = dupeString;
		final var surname = dupeString;

		return new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, givenName, surname))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(LocalDate.of(2000, 01, 01))
			.email("%s.%s@example.com".formatted(dupeString, dupeString).toLowerCase())
			.fileNumber(fileNumber)
			.givenName(givenName)
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
			.statusDate(generateStatusDate(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 01, 01)))
			.build();
	}

	protected PassportStatusEntity generatePassportTeamPassportStatus(String givenName, String surname, LocalDate dateOfBirth, String email, List<StatusCodeEntity> statusCodes) {
		final var fileNumber = generateFileNumber();

		final var passportStatus = new PassportStatusEntityBuilder()
			.id(generateId(fileNumber, givenName, surname))
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.givenName(givenName)
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
			.statusDate(generateStatusDate(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 01, 01)))
			.build();

		log.trace("Creating fake passport team status: {}", passportStatus);
		return passportStatus;
	}

	protected String generateApplicationRegisterSid(String fileNumber, String givenName, String surname) {
		return UUID.nameUUIDFromBytes(("applicationRegisterSid-%s-%s-%s".formatted(fileNumber, givenName, surname)).getBytes()).toString();
	}

	protected LocalDate generateDateOfBirth() {
		return faker.date().birthday().toLocalDateTime().toLocalDate();
	}

	protected String generateEmail(final String givenName, final String surname) {
		return stripDiacritics("%s.%s@example.com".formatted(givenName, surname)).toLowerCase();
	}

	protected String generateFileNumber() {
		return faker.random().hex(8);
	}

	protected String generateGivenName() {
		return faker.name().firstName();
	}

	protected String generateId(String fileNumber, String givenName, String surname) {
		return UUID.nameUUIDFromBytes(("id-%s-%s-%s".formatted(fileNumber, givenName, surname)).getBytes()).toString();
	}

	protected String generateSurname() {
		return faker.name().lastName();
	}

	protected StatusCodeEntity generateStatusCode(List<StatusCodeEntity> statusCodes) {
		Assert.notEmpty(statusCodes, "statusCodes is required; it must not be null and must contain at least one element");
		return statusCodes.get(faker.random().nextInt(statusCodes.size()));
	}

	protected LocalDate generateStatusDate(LocalDate start, LocalDate end) {
		final var zoneOffset = OffsetDateTime.now().getOffset();
		final var startDate = Timestamp.from(start.atStartOfDay().toInstant(zoneOffset));
		final var endDate = Timestamp.from(end.atStartOfDay().toInstant(zoneOffset));
		return faker.date().between(startDate, endDate).toLocalDateTime().toLocalDate();
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
