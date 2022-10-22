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
				.identificationCategoryText(CertificateApplicationModelMapper.FILE_NUMBER)
				.identificationId("🎸")
				.build())))
			.isNull();

		assertThat(mapper.findApplicationRegisterSid(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationModelMapper.APPLICATION_REGISTER_SID)
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
				.identificationCategoryText(CertificateApplicationModelMapper.APPLICATION_REGISTER_SID)
				.identificationId("🎸")
				.build())))
			.isNull();

		assertThat(mapper.findFileNumber(List.of(ImmutableCertificateApplicationIdentificationModel.builder()
				.identificationCategoryText(CertificateApplicationModelMapper.FILE_NUMBER)
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
	void testToDomain_null() throws Exception {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void testToDomain_nonnull() throws Exception {
		final var objectMapper = new ObjectMapper().findAndRegisterModules();

		// cheating a little here.. 😳
		final var createCertificateApplicationRequest = objectMapper.readValue("""
			{
			  "CertificateApplication": {
			    "CertificateApplicationApplicant": {
			      "BirthDate": { "Date": "2000-01-01" },
			      "PersonContactInformation": { "ContactEmailID": "user@example.com" },
			      "PersonName": {
			        "PersonGivenName": ["John"],
			        "PersonSurName": "Doe"
			      }
			    },
			    "CertificateApplicationDate": { "Date": "2000-01-01" },
			    "CertificateApplicationIdentification": [{
			      "IdentificationCategoryText": "Application Register SID",
			      "IdentificationID": "ABCD1234"
			    }, {
			      "IdentificationCategoryText": "File Number",
			      "IdentificationID": "ABCD1234"
			    }],
			    "CertificateApplicationStatus": { "StatusCode": "1" }
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
	}

	@Test
	void testToStatus() {
		// TODO :: GjB :: fix this test once the status mappings are known
		assertThat("https://open.spotify.com/track/3nBGFgfRQ8ujSmu5cGlZIU").isNotNull();
	}
}
