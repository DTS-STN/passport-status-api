package ca.gov.dtsstn.passport.api.web.model.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.service.SourceCodeService;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.SourceCode;
import ca.gov.dtsstn.passport.api.service.domain.StatusCode;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationApplicantModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationIdentificationModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationStatusModel;
import ca.gov.dtsstn.passport.api.web.model.CreateCertificateApplicationRequestModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationRepresentationModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationIdentificationModel;
import ca.gov.dtsstn.passport.api.web.model.SourceCodeModel;
import jakarta.annotation.PostConstruct;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper(componentModel = "spring")
public abstract class CertificateApplicationModelMapper {

	@Autowired
	protected SourceCodeService sourceCodeService;

	@Autowired
	protected StatusCodeService statusCodeService;

	@PostConstruct
	public void postConstruct() {
		Assert.notNull(sourceCodeService, "sourceCodeService is required; it must not be null");
		Assert.notNull(statusCodeService, "statusCodeService is required; it must not be null");
	}

	@Nullable
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "applicationRegisterSid", source = "certificateApplication.certificateApplicationIdentifications", qualifiedByName = { "findApplicationRegisterSid" })
	@Mapping(target = "dateOfBirth", source = "certificateApplication.certificateApplicationApplicant.birthDate.date")
	@Mapping(target = "email", source = "certificateApplication.certificateApplicationApplicant.personContactInformation.contactEmailId", qualifiedByName = { "emptyStringToNull" })
	@Mapping(target = "fileNumber", source = "certificateApplication.certificateApplicationIdentifications", qualifiedByName = { "findFileNumber" })
	@Mapping(target = "givenName", source = "certificateApplication.certificateApplicationApplicant.personName.personGivenNames", qualifiedByName = { "getFirstElement" })
	@Mapping(target = "manifestNumber", source = "certificateApplication.certificateApplicationIdentifications", qualifiedByName = { "findManifestNumber" })
	@Mapping(target = "surname", source = "certificateApplication.certificateApplicationApplicant.personName.personSurname")
	@Mapping(target = "sourceCodeId", source = "certificateApplication.resourceMeta.sourceCode", qualifiedByName = { "toSourceCodeId" })
	@Mapping(target = "statusCodeId", source = "certificateApplication.certificateApplicationStatus", qualifiedByName = { "toStatusCodeId" })
	@Mapping(target = "statusDate", source = "certificateApplication.certificateApplicationStatus.statusDate.date")
	@Mapping(target = "version", source = "certificateApplication.resourceMeta.version")
	public abstract PassportStatus toDomain(@Nullable CreateCertificateApplicationRequestModel createCertificateApplicationRequest);

	@Nullable
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "add", ignore = true) // fixes a weird vscode/eclipse & mapstruct bug quirk/bug 💩
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.birthDate.date", source = "dateOfBirth")
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personContactInformation.contactEmailId", source = "email")
	@Mapping(target = "certificateApplication.certificateApplicationIdentifications", source = "passportStatus", qualifiedByName = { "getCertificateApplicationIdentifications" })
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personName.personGivenNames", source = "passportStatus", qualifiedByName = { "getPersonGivenNames" })
	@Mapping(target = "certificateApplication.certificateApplicationApplicant.personName.personSurname", source = "surname")
	@Mapping(target = "certificateApplication.certificateApplicationStatus.statusCode", source = "statusCodeId", qualifiedByName = { "toStatusCdoCode" })
	@Mapping(target = "certificateApplication.certificateApplicationStatus.statusDate.date", source = "statusDate")
	@Mapping(target = "certificateApplication.resourceMeta.version", source = "version")
	public abstract GetCertificateApplicationRepresentationModel toModel(@Nullable PassportStatus passportStatus);

	/**
	 * Map an ISO 8601 compliant date string to a {@link LocalDate}.
	 * Throws a {@link DateTimeParseException} if the string is invalid.
	 */
	@Nullable
	protected LocalDate toLocalDate(@Nullable String date) {
		if (date == null) { return null; }
		return LocalDate.parse(date);
	}

	/**
	 * Map a {@link SourceCodeModel} to a {@link SourceCode#getId}.
	 * Returns {@code null} if {@code SourceCodeModel} is {@code null} or the source code cannot be found.
	 */
	@Nullable
	@Named("toSourceCodeId")
	protected String toSourceCodeId(@Nullable SourceCodeModel sourceCode) {
		return Optional.ofNullable(sourceCode)
			.map(SourceCodeModel::getReferenceDataId)
			.flatMap(sourceCodeService::readByCdoCode)
			.map(SourceCode::getId)
			.orElse(null);
	}

	/**
	 * Map a {@link PassportStatus#getSourceCodeId} to a {@link cdoCode}.
	 * Returns {@code null} if {@code sourceCodeId} is {@code null} or the source cannot be found.
	 */
	@Nullable
	@Named("toSourceCdoCode")
	protected String toSourceCdoCode(@Nullable String sourceCodeId) {
		return Optional.ofNullable(sourceCodeId)
			.flatMap(sourceCodeService::read)
			.map(SourceCode::getCdoCode)
			.orElse(null);
	}


	/**
	 * Map a {@link CertificateApplicationApplicantModel} to a {@link StatusCode#getId}.
	 * Returns {@code null} if {@code certificateApplicationStatusModel} is {@code null} or the status code cannot be found.
	 */
	@Nullable
	@Named("toStatusCodeId")
	protected String toStatusCodeId(@Nullable CertificateApplicationStatusModel certificateApplicationStatus) {
		return Optional.ofNullable(certificateApplicationStatus)
			.map(CertificateApplicationStatusModel::getStatusCode)
			.flatMap(statusCodeService::readByCdoCode)
			.map(StatusCode::getId)
			.orElse(null);
	}

	 /**
	 * Map a {@link PassportStatus#getStatusCodeId()} to a {@link cdoCode}.
	 * Returns {@code null} if {@code passportStatus} is {@code null} or the status cannot be found.
	 */
	@Nullable
	@Named("toStatusCdoCode")
	protected String toStatusCdoCode(@Nullable String statusCodeId) {
		return Optional.ofNullable(statusCodeId)
			.flatMap(statusCodeService::read)
			.map(StatusCode::getCdoCode)
			.orElse(null);
	}

	/**
	 * Finds the application registration SID element within {@code certificateApplicationIdentifications}. Returns null
	 * if not found or {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	@Named("findApplicationRegisterSid")
	protected String findApplicationRegisterSid(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		return findCertificateApplicationIdentification(certificateApplicationIdentifications, CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT);
	}

	@Nullable
	@Named("getCertificateApplicationIdentifications")
	protected List<CertificateApplicationIdentificationModel> getCertificateApplicationIdentifications(@Nullable PassportStatus passportStatus) {
		if (passportStatus == null) { return null; }

		final CertificateApplicationIdentificationModel applicationRegisterSid = Optional.ofNullable(passportStatus.getApplicationRegisterSid())
			.map(xxx -> ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT)
				.identificationId(passportStatus.getApplicationRegisterSid())
				.build())
			.orElse(null);

		final CertificateApplicationIdentificationModel fileNumber = Optional.ofNullable(passportStatus.getFileNumber())
			.map(xxx -> ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT)
				.identificationId(passportStatus.getFileNumber())
				.build())
			.orElse(null);

		final CertificateApplicationIdentificationModel manifestNumber = Optional.ofNullable(passportStatus.getManifestNumber())
			.map(xxx -> ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.MANIFEST_NUMBER_CATEGORY_TEXT)
				.identificationId(passportStatus.getManifestNumber())
				.build())
			.orElse(null);

		return Stream.of(applicationRegisterSid, fileNumber, manifestNumber).filter(Objects::nonNull).toList();
	}

	@Nullable
	@Named("getPersonGivenNames")
	protected List<String> getPersonGivenNames(@Nullable PassportStatus passportStatus) {
		return Optional.ofNullable(passportStatus)
			.map(PassportStatus::getGivenName)
			.map(List::of)
			.orElse(List.of());
	}

	/**
	 * Finds the {@code CertificateApplicationIdentificationModel} that has a matching {@code identificationCategoryText}
	 * value. Returns {@code null} if {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	protected String findCertificateApplicationIdentification(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications, String identificationCategoryText) {
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
	protected String findFileNumber(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		return findCertificateApplicationIdentification(certificateApplicationIdentifications, CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT);
	}

	/**
	 * Finds the manifest number element within {@code certificateApplicationIdentifications}. Returns null
	 * if not found or {@code certificateApplicationIdentifications} is null.
	 */
	@Nullable
	@Named("findManifestNumber")
	protected String findManifestNumber(@Nullable Iterable<CertificateApplicationIdentificationModel> certificateApplicationIdentifications) {
		return findCertificateApplicationIdentification(certificateApplicationIdentifications, CertificateApplicationIdentificationModel.MANIFEST_NUMBER_CATEGORY_TEXT);
	}

	/**
	 * Maps an {@code Iterable} to a single element by returning the first element found. Returns {@code null} if
	 * {@code iterable} is empty or {@code null}.
	 */
	@Nullable
	@Named("getFirstElement")
	protected <T> T getFirstElement(@Nullable Iterable<T> iterable) {
		return stream(iterable).findFirst().orElse(null);
	}

	/**
	 * A predicate that operates over {@code CertificateApplicationIdentificationModel}, returning {@code true} if
	 * {@code CertificateApplicationIdentificationModel.identificationCategoryText} equals {@code identificationCategoryText}.
	 * Returns {@code false} if {@code certificateApplicationIdentificationModel} is {@code null}.
	 */
	protected Predicate<CertificateApplicationIdentificationModel> hasIdentificationCategoryText(String identificationCategoryText) {
		Assert.hasText(identificationCategoryText, "identificationCategoryText is required; it must not be null or blank");
		return certificateApplicationIdentification -> Optional.ofNullable(certificateApplicationIdentification)
			.map(CertificateApplicationIdentificationModel::getIdentificationCategoryText)
			.map(identificationCategoryText::equals)
			.orElse(false);
	}

	/**
	 * Maps an {@link Iterable} to a {@link Stream}. Returns {@code Stream.empty()} if {@code iterable} is null.
	 */
	protected <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
		return Optional.ofNullable(iterable)
			.map(Iterable::spliterator)
			.map(spliterator -> StreamSupport.stream(spliterator, false))
			.orElse(Stream.empty());
	}

	@Nullable
	@Named("emptyStringToNull")
	protected String emptyStringToNull(@Nullable String string) {
		if (string == null) { return null; }
		if (string.length() == 0) { return null; }
		return string;
	}
}
