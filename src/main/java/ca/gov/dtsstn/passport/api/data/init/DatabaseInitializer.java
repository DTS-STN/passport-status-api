package ca.gov.dtsstn.passport.api.data.init;

import static java.time.temporal.ChronoUnit.MILLIS;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.PassportStatusRepository;
import ca.gov.dtsstn.passport.api.data.entity.DeliveryMethodCodeEntity;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntity;
import ca.gov.dtsstn.passport.api.data.entity.PassportStatusEntityBuilder;
import ca.gov.dtsstn.passport.api.data.entity.ServiceLevelCodeEntity;
import ca.gov.dtsstn.passport.api.data.entity.SourceCodeEntity;
import ca.gov.dtsstn.passport.api.data.entity.SourceCodeEntityBuilder;
import ca.gov.dtsstn.passport.api.data.entity.StatusCodeEntity;
import ca.gov.dtsstn.passport.api.service.DeliveryMethodCodeService;
import ca.gov.dtsstn.passport.api.service.ServiceLevelCodeService;
import ca.gov.dtsstn.passport.api.service.SourceCodeService;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.mapper.DeliveryMethodCodeMapper;
import ca.gov.dtsstn.passport.api.service.domain.mapper.ServiceLevelCodeMapper;
import ca.gov.dtsstn.passport.api.service.domain.mapper.SourceCodeMapper;
import ca.gov.dtsstn.passport.api.service.domain.mapper.StatusCodeMapper;
import net.datafaker.Faker;

/**
 * This class is intended to be used for development purposes only!
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class DatabaseInitializer {

	private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

	private static final String IRIS_ID = "327c25eb-e3f4-492e-bd47-4feb20189e78";

	private final Faker faker = new Faker(Locale.CANADA_FRENCH, new Random(0L));

	private final Pattern diacriticsPattern	= Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	private final PassportStatusRepository passportStatusRepository;

	private final StatusCodeService statusCodeService;
	private final StatusCodeMapper statusCodeMapper = Mappers.getMapper(StatusCodeMapper.class);

  private final SourceCodeService sourceCodeService;
	private final SourceCodeMapper sourceCodeMapper = Mappers.getMapper(SourceCodeMapper.class);

  private final DeliveryMethodCodeService deliveryMethodCodeService;
	private final DeliveryMethodCodeMapper deliveryMethodCodeMapper = Mappers.getMapper(DeliveryMethodCodeMapper.class);

  private final ServiceLevelCodeService serviceLevelCodeService;
	private final ServiceLevelCodeMapper serviceLevelCodeMapper = Mappers.getMapper(ServiceLevelCodeMapper.class);

	private int duplicateStatusesNumber = 10;

	private int generatedStatusesNumber = 1000;

	public DatabaseInitializer(PassportStatusRepository passportStatusRepository, StatusCodeService statusCodeService, SourceCodeService sourceCodeService, DeliveryMethodCodeService deliveryMethodCodeService, ServiceLevelCodeService serviceLevelCodeService) {
		Assert.notNull(passportStatusRepository, "passportStatusRepository is required; it must not be null");
		Assert.notNull(statusCodeService, "statusCodeService is required; it must not be null");
    Assert.notNull(sourceCodeService, "sourceCodeService is required; it must not be null");
    Assert.notNull(deliveryMethodCodeService, "deliveryMethodCodeService is required; it must not be null");
    Assert.notNull(serviceLevelCodeService, "serviceLevelCodeService is required; it must not be null");
		this.passportStatusRepository = passportStatusRepository;
		this.statusCodeService = statusCodeService;
    this.sourceCodeService = sourceCodeService;
    this.deliveryMethodCodeService = deliveryMethodCodeService;
    this.serviceLevelCodeService = serviceLevelCodeService;
	}

	@Async
	public void initializeData() {
		final var stopWatch = StopWatch.create();

		log.info("Deleting all passport statuses");
		passportStatusRepository.deleteAllInBatch();

		log.info("Fetch all active passport status codes");
		final var statusCodes = statusCodeService.readAllByIsActive(true).stream().map(statusCodeMapper::toEntity).toList();

    log.info("Fetch all active passport source codes");
		final var sourceCodes = sourceCodeService.readAllByIsActive(true).stream().map(sourceCodeMapper::toEntity).toList();

    log.info("Fetch all active passport delivery method codes");
		final var deliveryMethodCodes = deliveryMethodCodeService.readAllByIsActive(true).stream().map(deliveryMethodCodeMapper::toEntity).toList();

    log.info("Fetch all active passport service level codes");
		final var serviceLevelCodes = serviceLevelCodeService.readAllByIsActive(true).stream().map(serviceLevelCodeMapper::toEntity).toList();

		log.info("Generating {} fake random passport statuses", generatedStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var randomPassportStatuses = Stream.generate(() -> generateRandomPassportStatus(statusCodes, sourceCodes, deliveryMethodCodes, serviceLevelCodes)).limit(generatedStatusesNumber).toList();
		partition(randomPassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Fake random data created in {}ms", stopWatch.getDuration().get(MILLIS));

		log.info("Generating {} duplicate fake passport statuses", duplicateStatusesNumber);
		stopWatch.reset(); stopWatch.start();
		final var duplicatePassportStatuses = Stream.generate(() -> generateDuplicatePassportStatus(statusCodes)).limit(duplicateStatusesNumber).toList();
		partition(duplicatePassportStatuses, 10_000).forEach(passportStatusRepository::saveAll);
		log.info("Duplicate fake data created in {}ms", stopWatch.getDuration().get(MILLIS));
	}

	protected <T> List<List<T>> partition(List<T> passportStatuses, int size) {
		final var counter = new AtomicInteger();
		return List.copyOf(passportStatuses.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size)).values());
	}

	protected PassportStatusEntity generateRandomPassportStatus(List<StatusCodeEntity> statusCodes, List<SourceCodeEntity> sourceCodes, List<DeliveryMethodCodeEntity> deliveryMethodCodes, List<ServiceLevelCodeEntity> serviceLevelCodes) {
		final var fileNumber = generateFileNumber();
		final var givenName = generateGivenName();
		final var manifestNumber = generateManifestNumber();
		final var surname = generateSurname();

		// include manifest number in 1/3 of statuses
		final var includeManifestNumber = faker.number().numberBetween(0, 3) % 3 == 0;


    // Generate timeline data such that there's a 50% chance each step of the way.
    final var receivedDate = generateDate(LocalDate.of(2020, 01, 01), LocalDate.now());
    final var reviewedDate = (faker.number().numberBetween(1,3) % 2 == 0) ? generateDate(receivedDate, LocalDate.now()) : null;
    final var printedDate = ((faker.number().numberBetween(1,3) % 2 == 0) && (reviewedDate != null)) ? generateDate(reviewedDate, LocalDate.now()) : null;
    final var completedDate = ((faker.number().numberBetween(1,3) % 2 == 0) && (printedDate != null)) ? generateDate(printedDate, LocalDate.now()) : null;

		return new PassportStatusEntityBuilder()
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(generateDateOfBirth())
			.email(generateEmail(givenName, surname))
			.fileNumber(fileNumber)
			.givenName(givenName)
			.manifestNumber(includeManifestNumber ? manifestNumber : null)
			.sourceCode(generateSourceCode(sourceCodes))
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
      .deliveryMethodCode(generateDeliveryMethodCode(deliveryMethodCodes))
      .serviceLevelCode(generateServiceLevelCode(serviceLevelCodes)) 
			.statusDate(generateDate(LocalDate.of(2020, 01, 01), LocalDate.now()))
      .appReceivedDate(receivedDate)
      .appReviewedDate(reviewedDate)
      .appPrintedDate(printedDate)
      .appCompletedDate(completedDate)
			.version(faker.number().randomNumber())
			.build();
	}

	protected PassportStatusEntity generateDuplicatePassportStatus(List<StatusCodeEntity> statusCodes) {
		final var dupeString = "DUPE0000";
		final var fileNumber = dupeString;
		final var givenName = dupeString;
		final var manifestNumber = dupeString;
		final var surname = dupeString;

		return new PassportStatusEntityBuilder()
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(LocalDate.of(2000, 01, 01))
			.email("%s.%s@example.com".formatted(dupeString, dupeString).toLowerCase())
			.fileNumber(fileNumber)
			.givenName(givenName)
			.manifestNumber(manifestNumber)
			.sourceCode(new SourceCodeEntityBuilder().id(IRIS_ID).build())
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
			.statusDate(generateDate(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 01, 01)))
			.version(faker.number().randomNumber())
			.build();
	}

	protected PassportStatusEntity generatePassportTeamPassportStatus(String givenName, String surname, LocalDate dateOfBirth, String email, List<StatusCodeEntity> statusCodes) {
		final var fileNumber = generateFileNumber();

		final var passportStatus = new PassportStatusEntityBuilder()
			.applicationRegisterSid(generateApplicationRegisterSid(fileNumber, givenName, surname))
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.givenName(givenName)
			.manifestNumber(generateManifestNumber())
			.sourceCode(new SourceCodeEntityBuilder().id(IRIS_ID).build())
			.surname(surname)
			.statusCode(generateStatusCode(statusCodes))
			.statusDate(generateDate(LocalDate.of(2000, 01, 01), LocalDate.of(2000, 01, 01)))
			.version(faker.number().randomNumber())
			.build();

		log.trace("Creating fake passport team status: {}", passportStatus);
		return passportStatus;
	}

	protected String generateApplicationRegisterSid(String fileNumber, String givenName, String surname) {
		return UUID.nameUUIDFromBytes(("applicationRegisterSid-%s-%s-%s".formatted(fileNumber, givenName, surname)).getBytes()).toString();
	}

	protected LocalDate generateDateOfBirth() {
		return faker.timeAndDate().birthday();
	}

	protected String generateEmail(final String givenName, final String surname) {
		return stripDiacritics("%s.%s@example.com".formatted(givenName, surname)).toLowerCase();
	}

	protected String generateFileNumber() {
		// generates an 8 byte string that starts with a letter
		return faker.letterify("?%s".formatted(faker.random().hex(7)), true);
	}

	protected String generateGivenName() {
		return faker.name().firstName();
	}

	protected String generateManifestNumber() {
		return faker.random().hex(8);
	}

	protected String generateSurname() {
		return faker.name().lastName();
	}

	protected StatusCodeEntity generateStatusCode(List<StatusCodeEntity> statusCodes) {
		Assert.notEmpty(statusCodes, "statusCodes is required; it must not be null and must contain at least one element");
		return statusCodes.get(faker.random().nextInt(statusCodes.size()));
	}

  protected SourceCodeEntity generateSourceCode(List<SourceCodeEntity> sourceCodes) {
		Assert.notEmpty(sourceCodes, "sourceCodes is required; it must not be null and must contain at least one element");
		return sourceCodes.get(faker.random().nextInt(sourceCodes.size()));
	}

  protected DeliveryMethodCodeEntity generateDeliveryMethodCode(List<DeliveryMethodCodeEntity> deliveryMethodCodes) {
		Assert.notEmpty(deliveryMethodCodes, "deliveryMethodCodes is required; it must not be null and must contain at least one element");
		return deliveryMethodCodes.get(faker.random().nextInt(deliveryMethodCodes.size()));
	}

  protected ServiceLevelCodeEntity generateServiceLevelCode(List<ServiceLevelCodeEntity> serviceLevelCodes) {
		Assert.notEmpty(serviceLevelCodes, "serviceLevelCodes is required; it must not be null and must contain at least one element");
		return serviceLevelCodes.get(faker.random().nextInt(serviceLevelCodes.size()));
	}

	protected LocalDate generateDate(LocalDate start, LocalDate end) {
		final var zoneOffset = OffsetDateTime.now().getOffset();
		final var startDate = start.atStartOfDay().toInstant(zoneOffset);
		final var endDate = end.atStartOfDay().toInstant(zoneOffset);
		return faker.timeAndDate().between(startDate, endDate).atOffset(zoneOffset).toLocalDate();
	}

	protected String stripDiacritics(String string) {
		return diacriticsPattern.matcher(Normalizer.normalize(string, Normalizer.Form.NFD)).replaceAll("");
	}

	@Value("${application.database-initializer.generated-statuses-number}")
	public void setGeneratedStatusesNumber(int generatedStatusesNumber) {
		Assert.isTrue(generatedStatusesNumber >= 0, "application.dev.database-initializer.generated-statuses-number must be greater than or equal to zero");
		this.generatedStatusesNumber = generatedStatusesNumber;
	}

	@Value("${application.database-initializer.duplicate-statuses-number}")
	public void setDuplicateStatusesNumber(int duplicateStatusesNumber) {
		Assert.isTrue(duplicateStatusesNumber >= 0, "application.dev.database-initializer.duplicate-statuses-number must be greater than or equal to zero");
		this.duplicateStatusesNumber = duplicateStatusesNumber;
	}

}
