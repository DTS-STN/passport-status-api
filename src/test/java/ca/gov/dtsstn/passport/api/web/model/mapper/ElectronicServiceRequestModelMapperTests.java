package ca.gov.dtsstn.passport.api.web.model.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.CreateElectronicServiceRequestModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class ElectronicServiceRequestModelMapperTests {

	ElectronicServiceRequestModelMapper mapper = Mappers.getMapper(ElectronicServiceRequestModelMapper.class);

	@Test
	void testGetFirstElement_null() {
		assertThat(mapper.getFirstElement((List<PassportStatus>) null)).isNull();
	}

	@Test
	void testGetFirstElement_nonnull() {
		assertThat(mapper.getFirstElement(List.of("foo", "bar")))
			.isEqualTo("foo");
	}

	@Test
	void testStream_null() {
		assertThat(mapper.stream(null)).isEmpty();
	}

	@Test
	void testStream_nonnull() {
		assertThat(mapper.stream(List.of("foo", "bar")))
			.contains("foo", "bar");
	}

	@Test
	void testToDomain_null() {
		assertThat(mapper.toDomain(null)).isNull();
	}

	@Test
	void testToDomain_nonnull() throws Exception {
		final var objectMapper = new ObjectMapper().findAndRegisterModules();

		final var createElectronicServiceRequest = objectMapper.readValue("""
			{
			  "Client": {
			    "BirthDate": { "Date": "2000-01-01" },
			    "PersonContactInformation": { "ContactEmailID": "user@example.com" },
			    "PersonName": {
			      "PersonGivenName": [ "John" ],
			      "PersonSurName": "Doe"
			    }
			  }
			}
			""", CreateElectronicServiceRequestModel.class);

		final var passportStatus = mapper.toDomain(createElectronicServiceRequest);

		final var nonnullFields = new String[] {
			"dateOfBirth", "email", "givenName", "surname", "version"
		};

		assertThat(passportStatus)
			.hasAllNullFieldsOrPropertiesExcept(nonnullFields);
		assertThat(passportStatus)
			.extracting(PassportStatus::getDateOfBirth)
			.isEqualTo(LocalDate.of(2000, 01, 01));
		assertThat(passportStatus)
			.extracting(PassportStatus::getEmail)
			.isEqualTo("user@example.com");
		assertThat(passportStatus)
			.extracting(PassportStatus::getGivenName)
			.isEqualTo("John");
		assertThat(passportStatus)
			.extracting(PassportStatus::getSurname)
			.isEqualTo("Doe");
	}

}
