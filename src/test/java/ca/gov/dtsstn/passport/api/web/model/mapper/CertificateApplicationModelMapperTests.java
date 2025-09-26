package ca.gov.dtsstn.passport.api.web.model.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.service.DeliveryMethodCodeService;
import ca.gov.dtsstn.passport.api.service.ServiceLevelCodeService;
import ca.gov.dtsstn.passport.api.service.SourceCodeService;
import ca.gov.dtsstn.passport.api.service.StatusCodeService;
import ca.gov.dtsstn.passport.api.service.domain.ImmutableDeliveryMethodCode;
import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.ImmutableServiceLevelCode;
import ca.gov.dtsstn.passport.api.service.domain.ImmutableStatusCode;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.BirthDateModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationApplicantModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationIdentificationModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationDeliveryMethodModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationServiceLevelModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationStatusModel;
import ca.gov.dtsstn.passport.api.web.model.CertificateApplicationTimelineDateModel;
import ca.gov.dtsstn.passport.api.web.model.CreateCertificateApplicationRequestModel;
import ca.gov.dtsstn.passport.api.web.model.GetCertificateApplicationRepresentationModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationDeliveryMethodModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationIdentificationModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationServiceLevelModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationStatusModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCertificateApplicationTimelineDateModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableTimelineDateModel;
import ca.gov.dtsstn.passport.api.web.model.PersonContactInformationModel;
import ca.gov.dtsstn.passport.api.web.model.PersonNameModel;
import ca.gov.dtsstn.passport.api.web.model.StatusDateModel;


/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class CertificateApplicationModelMapperTests {

	private final static String STATUS_CODE__FILE_BEING_PROCESSED__ID = "57fe687e-50a6-411f-af63-2a659622127d";
  private final static String DELIVERY_METHOD_CODE__MAIL__ID = "8aefdf27-c4f1-43ab-9575-7f760478dd5d";
  private final static String SERVICE_LEVEL_CODE__TEN_DAYS__ID = "e5f40fad-ad83-4fe1-9bdb-beba987045cb";

	private final static String STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE = "1";
  private final static String DELIVERY_METHOD_CODE__MAIL__CDO_CODE = "1";
  private final static String SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE = "1";

	CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

	@Mock
	private SourceCodeService sourceCodeService;

	@Mock
	private StatusCodeService statusCodeService;

  @Mock
	private DeliveryMethodCodeService deliveryMethodCodeService;

  @Mock
  private ServiceLevelCodeService serviceLevelCodeService;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(mapper, "sourceCodeService", sourceCodeService);
		ReflectionTestUtils.setField(mapper, "statusCodeService", statusCodeService);
		ReflectionTestUtils.setField(mapper, "deliveryMethodCodeService", deliveryMethodCodeService);
		ReflectionTestUtils.setField(mapper, "serviceLevelCodeService", serviceLevelCodeService);
	}

	@Test
	void testFindApplicationRegisterSid_null() {
		assertThat(mapper.findApplicationRegisterSid(null)).isNull();
	}

	@Test
	void testFindApplicationRegisterSid_nonnull() {
		final var applicationRegisterSid = "https://open.spotify.com/track/6kiASFX63DwJ7grwKG2HUX";

		assertThat(mapper.findApplicationRegisterSid(List.of())).isNull();

		assertThat(mapper.findApplicationRegisterSid(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT)
				.identificationId("🎸")
				.build())))
			.isNull();

		assertThat(mapper.findApplicationRegisterSid(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT)
				.identificationId(applicationRegisterSid)
				.build())))
			.isEqualTo(applicationRegisterSid);
	}

	@Test
	void testFindCertificateApplicationIdentification_null() {
		final var identificationCategoryText = "https://open.spotify.com/track/2eruGPoyRDG5xdKxqju9EW";

		assertThatIllegalArgumentException().isThrownBy(() -> mapper.findCertificateApplicationIdentification(List.of(), null));
		assertThat(mapper.findCertificateApplicationIdentification(null, identificationCategoryText)).isNull();
	}

	@Test
	void testFindCertificateApplicationIdentification_nonnull() {
		final var identificationCategoryText = "https://open.spotify.com/track/27IRo2rYeizhRMDaNVplNM";

		assertThat(mapper.findCertificateApplicationIdentification(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText("🎧")
				.identificationId("🎶")
				.build()), identificationCategoryText))
			.isNull();


		assertThat(mapper.findCertificateApplicationIdentification(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(identificationCategoryText)
				.identificationId("🎼")
				.build()), identificationCategoryText))
			.isEqualTo("🎼");
	}

	@Test
	void testFindFileNumber_null() {
		assertThat(mapper.findFileNumber(null)).isNull();
	}

	@Test
	void testFindFileNumber_nonnull() {
		final var fileNumber = "https://open.spotify.com/track/7ovBUU08wGw5jiCcylRlx4";

		assertThat(mapper.findFileNumber(List.of())).isNull();

		assertThat(mapper.findFileNumber(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT)
				.identificationId("🎸")
				.build())))
			.isNull();

		assertThat(mapper.findFileNumber(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT)
				.identificationId(fileNumber)
				.build())))
			.isEqualTo(fileNumber);
	}

	@Test
	void testGetPersonGivenNames_null() {
		assertThat(mapper.getPersonGivenNames(null)).isNull();
	}

	@Test
	void testGetFirstElement_null() {
		assertThat(mapper.getFirstElement((Iterable<Object>) null)).isNull();
	}

	@Test
	void testGetFirstElement_nonnull() {
		final var firstElement = "https://open.spotify.com/track/0pKG1Q3SoMIyVSabmzDprG";
		assertThat(mapper.getFirstElement(List.of(firstElement, "🤘"))).isEqualTo(firstElement);
	}

	@Test
	void testHasIdentificationCategoryText_nullIdentificationCategoryText() {
		assertThatIllegalArgumentException().isThrownBy(() -> mapper.hasIdentificationCategoryText(null));
	}

	@Test
	void testHasIdentificationCategoryText_nonnullIdentificationCategoryText() {
		final var identificationCategoryText = "https://open.spotify.com/track/1vNoA9F5ASnlBISFekDmg3";

		final var predicate = mapper.hasIdentificationCategoryText(identificationCategoryText);
		assertThat(predicate).isNotNull();

		assertThat(predicate.test(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(identificationCategoryText)
				.identificationId("🍺")
				.build()))
			.isTrue();

		assertThat(predicate.test(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText("This will not match...")
				.identificationId("🍺")
				.build()))
			.isFalse();
	}

	@Test
	void testStream_null() {
		assertThat(mapper.stream(null)).isEmpty();
	}

	@Test
	void testStream_nonnull() {
		assertThat(mapper.stream(List.of("https://open.spotify.com/track/0mGPVqy7PYrke7w4M4rPu2"))).isNotEmpty();
	}

	@Test
	void testToModel_null() {
		assertThat(mapper.toModel(null)).isNull();
	}

	@Test
	void testToModel_nonnull() {
		when(statusCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableStatusCode.builder().cdoCode(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE).build()));
    when(deliveryMethodCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableDeliveryMethodCode.builder().cdoCode(DELIVERY_METHOD_CODE__MAIL__CDO_CODE).build()));
    when(serviceLevelCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableServiceLevelCode.builder().cdoCode(SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE).build()));

		final var applicationRegisterSid = "https://open.spotify.com/track/7GonnnalI2s19OCQO1J7Tf";
		final var dateOfBirth = LocalDate.of(2004, 12, 8);
		final var fileNumber = "https://open.spotify.com/track/1fZvEmAmWtsDSUjAgDhddU?";
		final var email = "user@example.com";
		final var givenName = "https://open.spotify.com/track/4hgl5gAnNjzJJjX7VEzQme";
		final var surname = "https://open.spotify.com/track/5uFQgThuwbNhFItxJczUgv";
		final var statusCodeId = STATUS_CODE__FILE_BEING_PROCESSED__ID;
    final var deliveryMethodCodeId = DELIVERY_METHOD_CODE__MAIL__ID;
    final var serviceLevelCodeId = SERVICE_LEVEL_CODE__TEN_DAYS__ID;
    final var appReceivedDate = LocalDate.of(2000, 01, 01);
    final var appReviewedDate = LocalDate.of(2000, 01, 02);
		final var statusDate = LocalDate.of(2000, 01, 01);

		final var passportStatus = ImmutablePassportStatus.builder()
			.applicationRegisterSid(applicationRegisterSid)
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.givenName(givenName)
			.surname(surname)
			.statusCodeId(statusCodeId)
      .deliveryMethodCodeId(deliveryMethodCodeId)
      .serviceLevelCodeId(serviceLevelCodeId)
      .appReceivedDate(appReceivedDate) 
      .appReviewedDate(appReviewedDate)
			.statusDate(statusDate)
			.build();

		final var getCertificateApplicationRepresentation = mapper.toModel(passportStatus);

		/*
		 * To future developers: I am sorry for this. This is what NIEM does to your code. 😔
		 */

		final var nullFields = new String[] {
			"id", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"
		};

		assertThat(getCertificateApplicationRepresentation)
			.hasNoNullFieldsOrPropertiesExcept(nullFields);
		assertThat(getCertificateApplicationRepresentation) // check dateOfBirth field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getBirthDate)
			.extracting(BirthDateModel::getDate)
			.isEqualTo(dateOfBirth.toString());
		assertThat(getCertificateApplicationRepresentation) // check applicationRegisterSid field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationIdentifications).asInstanceOf(LIST)
			.contains(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.APPLICATION_REGISTER_SID_CATEGORY_TEXT)
				.identificationId(applicationRegisterSid)
				.build());
		assertThat(getCertificateApplicationRepresentation) // check email field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonContactInformation)
			.extracting(PersonContactInformationModel::getContactEmailId)
			.isEqualTo(email);
		assertThat(getCertificateApplicationRepresentation) // check fileNumber field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationIdentifications).asInstanceOf(LIST)
			.contains(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT)
				.identificationId(fileNumber)
				.build());
		assertThat(getCertificateApplicationRepresentation) // check givenName field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonGivenNames).asInstanceOf(LIST)
			.contains(givenName);
		assertThat(getCertificateApplicationRepresentation) // check surname field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonSurname)
			.isEqualTo(surname);
		assertThat(getCertificateApplicationRepresentation) // check status field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationStatus)
			.extracting(CertificateApplicationStatusModel::getStatusCode)
			.isEqualTo(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE);
    assertThat(getCertificateApplicationRepresentation) // check delivery method field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationDeliveryMethod)
			.extracting(CertificateApplicationDeliveryMethodModel::getDeliveryMethodCode)
			.isEqualTo(DELIVERY_METHOD_CODE__MAIL__CDO_CODE);
    assertThat(getCertificateApplicationRepresentation) // check service level field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationServiceLevel)
			.extracting(CertificateApplicationServiceLevelModel::getServiceLevelCode)
			.isEqualTo(SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE);
    assertThat(getCertificateApplicationRepresentation) // check service level field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationTimelineDates)
      .asInstanceOf(InstanceOfAssertFactories.LIST)
      .contains(ImmutableCertificateApplicationTimelineDateModel.builder().referenceDataName(CertificateApplicationTimelineDateModel.RECEIVED_REFERENCE_DATA_TEXT).timelineDate(ImmutableTimelineDateModel.builder().date("2000-01-01").build()).build());
    assertThat(getCertificateApplicationRepresentation) // check service level field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationTimelineDates)
      .asInstanceOf(InstanceOfAssertFactories.LIST)
      .contains(ImmutableCertificateApplicationTimelineDateModel.builder().referenceDataName(CertificateApplicationTimelineDateModel.REVIEWED_REFERENCE_DATA_TEXT).timelineDate(ImmutableTimelineDateModel.builder().date("2000-01-02").build()).build());
		assertThat(getCertificateApplicationRepresentation) // check status field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationStatus)
			.extracting(CertificateApplicationStatusModel::getStatusDate)
			.extracting(StatusDateModel::getDate)
			.isEqualTo(statusDate.toString());

		verify(statusCodeService, times(2)).read(any());
	}

	@Test
	void testToModel_mononym() {
		final String givenName = null;
		final var surname = "https://open.spotify.com/track/5uFQgThuwbNhFItxJczUgv";

		final var passportStatus = ImmutablePassportStatus.builder()
			.givenName(givenName)
			.surname(surname)
			.build();

		final var getCertificateApplicationRepresentation = mapper.toModel(passportStatus);

		assertThat(getCertificateApplicationRepresentation) // check givenName field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonGivenNames)
			.isNull();
		assertThat(getCertificateApplicationRepresentation) // check surname field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonSurname)
			.isEqualTo(surname);
	}

	@Test
	void testToDomain_null() throws Exception {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void testToDomain_nonnull() throws Exception {
		when(statusCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableStatusCode.builder().id(STATUS_CODE__FILE_BEING_PROCESSED__ID).build()));

    when(deliveryMethodCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableDeliveryMethodCode.builder().id(DELIVERY_METHOD_CODE__MAIL__ID).build()));

    when(serviceLevelCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableServiceLevelCode.builder().id(SERVICE_LEVEL_CODE__TEN_DAYS__ID).build()));

		final var objectMapper = new ObjectMapper().findAndRegisterModules();

		// cheating a little here because doing anything with NIEM sucks.. 😳
		final var json = """
			{
			  "CertificateApplication": {
			    "CertificateApplicationApplicant": {
			      "BirthDate": { "Date": "2000-01-01" },
			      "PersonContactInformation": { "ContactEmailID": "user@example.com" },
			      "PersonName": {
			        "PersonGivenName": [ "John" ],
			        "PersonSurName": "Doe"
			      }
			    },
			    "CertificateApplicationIdentification": [{
			      "IdentificationCategoryText": "Application Register SID",
			      "IdentificationID": "ABCD1234"
			    }, {
			      "IdentificationCategoryText": "File Number",
			      "IdentificationID": "ABCD1234"
			    }],
			    "CertificateApplicationStatus": {
			      "StatusCode": "%s",
			      "StatusDate": "2000-01-01"
			    },
          "CertificateApplicationDeliveryMethod": {
			      "DeliveryMethodCode": "%s"
			    },
          "CertificateApplicationServiceLevel": {
			      "ServiceLevelCode": "%s"
			    },
          "CertificateApplicationTimelineDates": [
            {
              "ReferenceDataName": "Received",
              "TimelineDate": {
                "Date": "2021-01-01"
              }
            }, 
            {
              "ReferenceDataName": "Reviewed",
              "TimelineDate": {
                "Date": "2021-01-02"
              }
            }
          ]
			  }
			}
		""".formatted(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE, DELIVERY_METHOD_CODE__MAIL__ID, SERVICE_LEVEL_CODE__TEN_DAYS__ID);

		final var createCertificateApplicationRequest = objectMapper.readValue(json, CreateCertificateApplicationRequestModel.class);

		final var passportStatus = mapper.toDomain(createCertificateApplicationRequest);

		final var nonnullFields = new String[] {
			"applicationRegisterSid", "dateOfBirth", "email", "fileNumber", "givenName", "surname", "statusCodeId", "deliveryMethodCodeId", "serviceLevelCodeId", "appReceivedDate", "appReviewedDate", "statusDate", "version"
		};

		assertThat(passportStatus)
			.hasAllNullFieldsOrPropertiesExcept(nonnullFields);
		assertThat(passportStatus)
			.extracting(PassportStatus::getApplicationRegisterSid)
			.isEqualTo("ABCD1234");
		assertThat(passportStatus)
			.extracting(PassportStatus::getDateOfBirth)
			.isEqualTo(LocalDate.of(2000, 01, 01));
		assertThat(passportStatus)
			.extracting(PassportStatus::getEmail)
			.isEqualTo("user@example.com");
		assertThat(passportStatus)
			.extracting(PassportStatus::getFileNumber)
			.isEqualTo("ABCD1234");
		assertThat(passportStatus)
			.extracting(PassportStatus::getGivenName)
			.isEqualTo("John");
		assertThat(passportStatus)
			.extracting(PassportStatus::getSurname)
			.isEqualTo("Doe");
		assertThat(passportStatus)
			.extracting(PassportStatus::getStatusCodeId)
			.isEqualTo(STATUS_CODE__FILE_BEING_PROCESSED__ID);
    assertThat(passportStatus)
			.extracting(PassportStatus::getDeliveryMethodCodeId)
			.isEqualTo(DELIVERY_METHOD_CODE__MAIL__ID);
    assertThat(passportStatus)
			.extracting(PassportStatus::getServiceLevelCodeId)
			.isEqualTo(SERVICE_LEVEL_CODE__TEN_DAYS__ID);
    assertThat(passportStatus)
			.extracting(PassportStatus::getAppReceivedDate)
			.isEqualTo(LocalDate.of(2021, 01, 01));
    assertThat(passportStatus)
			.extracting(PassportStatus::getAppReviewedDate)
			.isEqualTo(LocalDate.of(2021, 01, 02));
		assertThat(passportStatus)
			.extracting(PassportStatus::getStatusDate)
			.isEqualTo(LocalDate.of(2000, 01, 01));

		verify(statusCodeService).readByCdoCode(any());
    verify(deliveryMethodCodeService).readByCdoCode(any());
    verify(serviceLevelCodeService).readByCdoCode(any());
	}

	@Test
	void testToDomain_mononym() throws Exception {
		when(statusCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableStatusCode.builder().id(STATUS_CODE__FILE_BEING_PROCESSED__ID).build()));

    when(deliveryMethodCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableDeliveryMethodCode.builder().id(DELIVERY_METHOD_CODE__MAIL__ID).build()));

    when(serviceLevelCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableServiceLevelCode.builder().id(SERVICE_LEVEL_CODE__TEN_DAYS__ID).build()));

		final var objectMapper = new ObjectMapper().findAndRegisterModules();

		// cheating a little here because doing anything with NIEM sucks.. 😳
		final var json = """
			{
			  "CertificateApplication": {
			    "CertificateApplicationApplicant": {
			      "BirthDate": { "Date": "2000-01-01" },
			      "PersonContactInformation": { "ContactEmailID": "user@example.com" },
			      "PersonName": {
			        "PersonSurName": "Doe"
			      }
			    },
			    "CertificateApplicationIdentification": [{
			      "IdentificationCategoryText": "Application Register SID",
			      "IdentificationID": "ABCD1234"
			    }, {
			      "IdentificationCategoryText": "File Number",
			      "IdentificationID": "ABCD1234"
			    }],
			    "CertificateApplicationStatus": {
			      "StatusCode": "%s",
			      "StatusDate": "2000-01-01"
			    },
          "CertificateApplicationDeliveryMethod": {
			      "DeliveryMethodCode": "%s"
			    },
          "CertificateApplicationServiceLevel": {
			      "ServiceLevelCode": "%s"
			    },
          "CertificateApplicationTimelineDates": [
            {
              "ReferenceDataName": "Received",
              "TimelineDate": {
                "Date": "2021-01-01"
              }
            }, 
            {
              "ReferenceDataName": "Reviewed",
              "TimelineDate": {
                "Date": "2021-01-02"
              }
            }
          ]
			  }
			}
		""".formatted(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE, DELIVERY_METHOD_CODE__MAIL__ID, SERVICE_LEVEL_CODE__TEN_DAYS__ID);

		final var createCertificateApplicationRequest = objectMapper.readValue(json, CreateCertificateApplicationRequestModel.class);

		final var passportStatus = mapper.toDomain(createCertificateApplicationRequest);

		assertThat(passportStatus)
			.extracting(PassportStatus::getGivenName)
			.isNull();
		assertThat(passportStatus)
			.extracting(PassportStatus::getSurname)
			.isEqualTo("Doe");
	}

	@Test
	void testToStatusCodeId() {
		// arrange
		when(statusCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableStatusCode.builder().id(STATUS_CODE__FILE_BEING_PROCESSED__ID).build()));

		final var certificateApplicationStatusModel = ImmutableCertificateApplicationStatusModel.builder().statusCode(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE).build();

		// act
		final var act = mapper.toStatusCodeId(certificateApplicationStatusModel);

		// assert
		assertThat(act).isEqualTo(STATUS_CODE__FILE_BEING_PROCESSED__ID);
		verify(statusCodeService).readByCdoCode(any());
	}

	@Test
	void testToStatusCdoCode() {
		// arrange
		when(statusCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableStatusCode.builder().cdoCode(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE).build()));

		// act
		final var act = mapper.toStatusCdoCode(STATUS_CODE__FILE_BEING_PROCESSED__ID);

		// assert
		assertThat(act).isEqualTo(STATUS_CODE__FILE_BEING_PROCESSED__CDO_CODE);
		verify(statusCodeService).read(any());
	}

  @Test
	void testToDeliveryMethodCodeId() {
		// arrange
		when(deliveryMethodCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableDeliveryMethodCode.builder().id(DELIVERY_METHOD_CODE__MAIL__ID).build()));

		final var certificateApplicationDeliveryMethodModel = ImmutableCertificateApplicationDeliveryMethodModel.builder().deliveryMethodCode(DELIVERY_METHOD_CODE__MAIL__CDO_CODE).build();

		// act
		final var act = mapper.toDeliveryMethodCodeId(certificateApplicationDeliveryMethodModel);

		// assert
		assertThat(act).isEqualTo(DELIVERY_METHOD_CODE__MAIL__ID);
		verify(deliveryMethodCodeService).readByCdoCode(any());
	}

	@Test
	void testToDeliveryMethodCdoCode() {
		// arrange
		when(deliveryMethodCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableDeliveryMethodCode.builder().cdoCode(DELIVERY_METHOD_CODE__MAIL__CDO_CODE).build()));

		// act
		final var act = mapper.toDeliveryMethodCdoCode(DELIVERY_METHOD_CODE__MAIL__ID);

		// assert
		assertThat(act).isEqualTo(DELIVERY_METHOD_CODE__MAIL__CDO_CODE);
		verify(deliveryMethodCodeService).read(any());
	}

  @Test
	void testToServiceLevelCodeId() {
		// arrange
		when(serviceLevelCodeService.readByCdoCode(any())).thenReturn(Optional.ofNullable(ImmutableServiceLevelCode.builder().id(SERVICE_LEVEL_CODE__TEN_DAYS__ID).build()));

		final var certificateApplicationServiceLevelModel = ImmutableCertificateApplicationServiceLevelModel.builder().serviceLevelCode(SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE).build();

		// act
		final var act = mapper.toServiceLevelCodeId(certificateApplicationServiceLevelModel);

		// assert
		assertThat(act).isEqualTo(SERVICE_LEVEL_CODE__TEN_DAYS__ID);
		verify(serviceLevelCodeService).readByCdoCode(any());
	}

	@Test
	void testToServiceLevelCdoCode() {
		// arrange
		when(serviceLevelCodeService.read(any())).thenReturn(Optional.ofNullable(ImmutableServiceLevelCode.builder().cdoCode(SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE).build()));

		// act
		final var act = mapper.toServiceLevelCdoCode(SERVICE_LEVEL_CODE__TEN_DAYS__ID);

		// assert
		assertThat(act).isEqualTo(SERVICE_LEVEL_CODE__TEN_DAYS__CDO_CODE);
		verify(serviceLevelCodeService).read(any());
	}
}
