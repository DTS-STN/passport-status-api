package ca.gov.dtsstn.passport.api.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;


/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class CertificateApplicationModelMapperTests {

	CertificateApplicationModelMapper mapper = Mappers.getMapper(CertificateApplicationModelMapper.class);

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
		assertThat(mapper.toModel(null)).isEqualTo(null);
	}

	@Test
	void testToModel_nonnull() {
		final var applicationRegisterSid = "https://open.spotify.com/track/7GonnnalI2s19OCQO1J7Tf";
		final var dateOfBirth = LocalDate.of(2004, 12, 8);
		final var fileNumber = "https://open.spotify.com/track/1fZvEmAmWtsDSUjAgDhddU?";
		final var email = "user@example.com";
		final var firstName = "https://open.spotify.com/track/4hgl5gAnNjzJJjX7VEzQme";
		final var lastName = "https://open.spotify.com/track/5uFQgThuwbNhFItxJczUgv";
		final var status = PassportStatus.Status.APPROVED;
		final var statusDate = LocalDate.of(2000, 01, 01);

		final var passportStatus = ImmutablePassportStatus.builder()
			.applicationRegisterSid(applicationRegisterSid)
			.dateOfBirth(dateOfBirth)
			.email(email)
			.fileNumber(fileNumber)
			.firstName(firstName)
			.lastName(lastName)
			.status(status)
			.statusDate(statusDate)
			.build();

		final var getCertificateApplicationRepresentation = mapper.toModel(passportStatus);

		/*
		 * To future developers: I am sorry for this. This is what NIEM does to your code. 😔
		 */

		assertThat(getCertificateApplicationRepresentation).isNotNull();
		assertThat(getCertificateApplicationRepresentation) // check dateOfBirth field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getBirthDate)
			.extracting(BirthDateModel::getDate)
			.isEqualTo(dateOfBirth.toString());
		assertThat(getCertificateApplicationRepresentation) // check applicationRegisterSid field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationIdentifications).asList()
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
			.extracting(CertificateApplicationModel::getCertificateApplicationIdentifications).asList()
			.contains(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationIdentificationModel.FILE_NUMBER_CATEGORY_TEXT)
				.identificationId(fileNumber)
				.build());
		assertThat(getCertificateApplicationRepresentation) // check firstName field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonGivenNames).asList()
			.contains(firstName);
		assertThat(getCertificateApplicationRepresentation) // check lastName field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationApplicant)
			.extracting(CertificateApplicationApplicantModel::getPersonName)
			.extracting(PersonNameModel::getPersonSurname)
			.isEqualTo(lastName);
		assertThat(getCertificateApplicationRepresentation) // check status field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationStatus)
			.extracting(CertificateApplicationStatusModel::getStatusCode)
			.isNotNull(); // TODO :: GjB :: fix this check when status code mappings are known
		assertThat(getCertificateApplicationRepresentation) // check status field
			.extracting(GetCertificateApplicationRepresentationModel::getCertificateApplication)
			.extracting(CertificateApplicationModel::getCertificateApplicationStatus)
			.extracting(CertificateApplicationStatusModel::getStatusDate)
			.extracting(StatusDateModel::getDate)
			.isEqualTo(statusDate.toString());
	}

	@Test
	void testToDomain_null() throws Exception {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void testToDomain_nonnull() throws Exception {
		final var objectMapper = new ObjectMapper().findAndRegisterModules();

		// cheating a little here because doing anything with NIEM sucks.. 😳
		final var createCertificateApplicationRequest = objectMapper.readValue("""
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
			      "StatusCode": "1",
			      "StatusDate": "2000-01-01"
			    }
			  }
			}
			""", CreateCertificateApplicationRequestModel.class);

		final var passportStatus = mapper.toDomain(createCertificateApplicationRequest);

		assertThat(passportStatus).isNotNull();
		assertThat(passportStatus.getApplicationRegisterSid()).isEqualTo("ABCD1234");
		assertThat(passportStatus.getDateOfBirth()).isEqualTo(LocalDate.of(2000, 01, 01));
		assertThat(passportStatus.getEmail()).isEqualTo("user@example.com");
		assertThat(passportStatus.getFileNumber()).isEqualTo("ABCD1234");
		assertThat(passportStatus.getFirstName()).isEqualTo("John");
		assertThat(passportStatus.getLastName()).isEqualTo("Doe");
		assertThat(passportStatus.getStatus()).isEqualTo(PassportStatus.Status.APPROVED);
		assertThat(passportStatus.getStatusDate()).isEqualTo(LocalDate.of(2000, 01, 01));
	}

	@Test
	void testToStatus() {
		// TODO :: GjB :: fix this test once the status mappings are known
		assertThat("https://open.spotify.com/track/3nBGFgfRQ8ujSmu5cGlZIU").isNotNull();
	}
}
