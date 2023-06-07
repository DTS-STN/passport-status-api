package ca.gov.dtsstn.passport.api.web.model.mapper;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.CreateElectronicServiceRequestModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Mapper
public interface ElectronicServiceRequestModelMapper {

	@Nullable
	@Mapping(target = "applicationRegisterSid", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "fileNumber", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	@Mapping(target = "manifestNumber", ignore = true)
	@Mapping(target = "sourceCodeId", ignore = true)
	@Mapping(target = "statusCodeId", ignore = true)
	@Mapping(target = "statusDate", ignore = true)
	@Mapping(target = "dateOfBirth", source = "client.personBirthDate.date")
	@Mapping(target = "email", source = "client.personContactInformation.contactEmailId")
	@Mapping(target = "givenName", source = "client.personName.personGivenNames", qualifiedByName = { "getFirstElement" })
	@Mapping(target = "surname", source = "client.personName.personSurname")
	@Mapping(target = "version", constant = "0L") // TODO :: XXX :: GjB fix this when we have the correct NIEM field name
	PassportStatus toDomain(@Nullable CreateElectronicServiceRequestModel createElectronicServiceRequest);

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
	 * Maps an {@link Iterable} to a {@link Stream}. Returns {@code Stream.empty()} if {@code iterable} is null.
	 */
	default <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
		return Optional.ofNullable(iterable)
			.map(Iterable::spliterator)
			.map(spliterator -> StreamSupport.stream(spliterator, false))
			.orElse(Stream.empty());
	}

}
