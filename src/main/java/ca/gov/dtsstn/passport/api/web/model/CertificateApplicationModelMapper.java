package ca.gov.dtsstn.passport.api.web.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus.Status;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface CertificateApplicationModelMapper {

	public static final String APPLICATION_REGISTER_SID = "Application Register SID";

	public static final String FILE_NUMBER = "File Number";

	// TODO :: GjB :: remove this once actual code → status mappings are known
	final Map<String, PassportStatus.Status> statusMap = Map.of(
		"1", PassportStatus.Status.APPROVED,
		"2", PassportStatus.Status.IN_EXAMINATION,
		"3", PassportStatus.Status.REJECTED
	);

	@Nullable
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "applicationRegisterSid", source = "certificateApplication.certificateApplicationIdentifications", qualifiedByName = { "findApplicationRegisterSid" })
	@Mapping(target = "dateOfBirth", source = "certificateApplication.certificateApplicationApplicant.birthDate.date")
	@Mapping(target = "email", source = "certificateApplication.certificateApplicationApplicant.personContactInformation.contactEmailId")
	@Mapping(target = "fileNumber", source = "certificateApplication.certificateApplicationIdentifications", qualifiedByName = { "findFileNumber" })
	@Mapping(target = "firstName", source = "certificateApplication.certificateApplicationApplicant.personName.personGivenNames", qualifiedByName = { "getFirstElement" })
	@Mapping(target = "lastName", source = "certificateApplication.certificateApplicationApplicant.personName.personSurname")
	@Mapping(target = "status", source = "certificateApplication.certificateApplicationStatus", qualifiedByName = { "toStatus" })
	PassportStatus toDomain(@Nullable CreateCertificateApplicationRequestModel createCertificateApplicationRequest);

	@Nullable
	@Mapping(target = "add", ignore = true) // fixes a weird vscode/eclipse & mapstruct bug quirk/bug 💩
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.birthDate.date", source = "dateOfBirth")
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personContactInformation.contactEmailId", source = "email")
	@Mapping(target = "certificateApplication.certificateApplicationIdentifications", source = "passportStatus", qualifiedByName = { "getCertificateApplicationIdentifications" })
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personName.personGivenNames", source = "passportStatus", qualifiedByName = { "getPersonGivenNames" })
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personName.personSurname", source = "lastName")
	@Mapping(target = "certificateApplication.certificateApplicationStatus", source = "status", qualifiedByName = { "toStatus" })
	GetCertificateApplicationRepresentationModel toModel(@Nullable PassportStatus passportStatus);

	/**
	 * Map a {@link CertificateApplicationApplicantModel} to a {@link PassportStatus.Status}. Returns {@code null} if
	 * {@code certificateApplicationStatusModel} is null or the status cannot be found.
	 */
	@Nullable
	@Named("toStatus")
	default PassportStatus.Status toStatus(@Nullable CertificateApplicationStatusModel certificateApplicationStatus) {
		return Optional.ofNullable(certificateApplicationStatus)
			.map(CertificateApplicationStatusModel::getStatusCode)
			.map(statusMap::get)
			.orElse(null);
	}

	/**
	 * Map a {@link PassportStatus.Status} to a {@link CertificateApplicationStatusModel}. Returns {@code null} if
	 * {@code passportStatus} is null or the status cannot be found.
	 */
	@Nullable
	@Named("toStatus")
	default CertificateApplicationStatusModel toStatus(@Nullable PassportStatus.Status passportStatus) {
		final Function<PassportStatus.Status, String> toStatusCode = status -> statusMap.entrySet().stream()
			.filter(entry -> entry.getValue().equals(status))
			.map(Entry<String, Status>::getKey)
			.findFirst().orElse(null);

		return Optional.ofNullable(passportStatus)
			.map(toStatusCode)
			.map(statusCode -> ImmutableCertificateApplicationStatusModel.builder()
				.statusCode(statusCode)
				.build())
			.orElse(null);
	}

	/**
	 * Finds the application registration SID element within {@code certificateApplicationIdentifications}. Returns null
	 * if not found or {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	@Named("findApplicationRegisterSid")
	default String findApplicationRegisterSid(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		return findCertificateApplicationIdentification(certificateApplicationIdentifications, APPLICATION_REGISTER_SID);
	}

	@Nullable
	@Named("getCertificateApplicationIdentifications")
	default List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications(@Nullable PassportStatus passportStatus) {
		if (passportStatus == null) { return null; }

		final CertificateApplicationIdentificationModel applicationRegisterSid = Optional.ofNullable(passportStatus.getApplicationRegisterSid())
			.map(xxx -> ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(APPLICATION_REGISTER_SID)
				.identificationId(passportStatus.getApplicationRegisterSid())
				.build())
			.orElse(null);

		final CertificateApplicationIdentificationModel fileNumber = Optional.ofNullable(passportStatus.getFileNumber())
			.map(x -> ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(FILE_NUMBER)
				.identificationId(passportStatus.getFileNumber())
				.build())
			.orElse(null);

		return Stream.of(applicationRegisterSid, fileNumber).filter(Objects::nonNull).toList();
	}

	@Nullable
	@Named("getPersonGivenNames")
	default List<String> getPersonGivenNames(@Nullable PassportStatus passportStatus) {
		return Optional.ofNullable(passportStatus)
			.map(PassportStatus::getFirstName)
			.map(List::of)
			.orElse(List.of());
	}

	/**
	 * Finds the {@code CertificateApplicationIdentificationModel} that has a matching {@code identificationCategoryText}
	 * value. Returns {@code null} if {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	default String findCertificateApplicationIdentification(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications, String identificationCategoryText) {
		Assert.hasText(identificationCategoryText, "identificationCategoryText is required; it must not be null or blank");
		return stream(certificateApplicationIdentifications)
			.filter(hasIdentificationCategoryText(identificationCategoryText))
			.map(CertificateApplicationIdentificationModel::getIdentificationId)
			.findFirst().orElse(null);
	}

	/**
	 * Finds the file number element within {@code certificateApplicationIdentifications}. Returns null if not found or
	 * {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	@Named("findFileNumber")
	default String findFileNumber(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		return findCertificateApplicationIdentification(certificateApplicationIdentifications, FILE_NUMBER);
	}

	/**
	 * Maps an {@code Iterable} to a single element by returning the first element found. Returns {@code null} if
	 * {@code iterable} is empty or {@code null}.
	 */
	@Nullable
	@Named("getFirstElement")
	default <T> T getFirstElement(@Nullable Iterable<T> iterable) {
		return stream(iterable).findFirst().orElse(null);
	}

	/**
	 * A predicate that operates over {@code CertificateApplicationIdentificationModel}, returning {@code true} if
	 * {@code CertificateApplicationIdentificationModel.identificationCategoryText} equals {@code identificationCategoryText}.
	 * Returns {@code false} if {@code certificateApplicationIdentificationModel} is {@code null}.
	 */
	default Predicate<CertificateApplicationIdentificationModel> hasIdentificationCategoryText(String identificationCategoryText) {
		Assert.hasText(identificationCategoryText, "identificationCategoryText is required; it must not be null or blank");
		return certificateApplicationIdentification -> Optional.ofNullable(certificateApplicationIdentification)
			.map(CertificateApplicationIdentificationModel::getIdentificationCategoryText)
			.map(identificationCategoryText::equals)
			.orElse(false);
	}

	/**
	 * Maps an {@link Iterable} to a {@link Stream}. Returns {@code Stream.empty()} if {@code iterable} is null.
	 */
	default <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
		return Optional.ofNullable(iterable)
			.map(Iterable::spliterator)
			.map(spliterator -> StreamSupport.stream(spliterator, false))
			.orElse(Stream.empty());
	}

}
