package ca.gov.dtsstn.passport.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ClassUtils;

import ca.gov.dtsstn.passport.api.event.NotificationNotSentEvent;
import ca.gov.dtsstn.passport.api.event.NotificationRequestedEvent;
import ca.gov.dtsstn.passport.api.event.NotificationSentEvent;
import ca.gov.dtsstn.passport.api.service.NotificationService;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.service.domain.ImmutablePassportStatus;
import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;
import ca.gov.dtsstn.passport.api.web.model.ImmutableBirthDateModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableClientModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableCreateElectronicServiceRequestModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutablePersonContactInformationModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutablePersonNameModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutablePersonPreferredLanguageModel;
import ca.gov.dtsstn.passport.api.web.model.PersonPreferredLanguageModel.LanguageName;

/**
 * Tests for {@link ElectronicServiceRequestController}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class ElectronicServiceRequestControllerTests {

	ElectronicServiceRequestController controller;

	@Mock
	ApplicationEventPublisher eventPublisher;

	@Mock
	NotificationService notificationService;

	@Mock
	PassportStatusService passportStatusService;

	/**
	 * A basic ESRF request used throughout the test
	 */
	final ImmutableCreateElectronicServiceRequestModel electronicServiceRequest = ImmutableCreateElectronicServiceRequestModel.builder()
		.client(ImmutableClientModel.builder()
			.personBirthDate(ImmutableBirthDateModel.builder()
				.date("2000-01-01")
				.build())
			.personContactInformation(ImmutablePersonContactInformationModel.builder()
				.contactEmailId("user@example.com")
				.build())
			.personName(ImmutablePersonNameModel.builder()
				.addPersonGivenNames("John")
				.personSurname("Doe")
				.build())
			.personPreferredLanguage(ImmutablePersonPreferredLanguageModel.builder()
				.languageName(LanguageName.ENGLISH.toString())
				.build())
			.build())
		.build();

	@BeforeEach
	void beforeEach() {
		this.controller = new ElectronicServiceRequestController(eventPublisher, notificationService, passportStatusService);
	}

	@Test
	@DisplayName("ESRF request with zero results does not send notification")
	void testCreate_noSearchResults() {
		// the email search must return zero statuses
		final var searchResults = List.<PassportStatus> of();
		when(passportStatusService.emailSearch(any(), any(), any(), any())).thenReturn(searchResults);

		// fire the target controller method!
		controller.create(electronicServiceRequest);

		// verify that no notifications were sent
		verifyNoInteractions(notificationService);

		// verify the correct number of events were fired, capturing the events in the process
		final var eventCaptor = ArgumentCaptor.forClass(Object.class);
		verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

		assertThat(eventCaptor).extracting(ArgumentCaptor::getAllValues).asInstanceOf(LIST)
			// verify that the correct event types were fired
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationRequestedEvent.class, event))
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationNotSentEvent.class, event))
			// verify the reason string in the NotificationNotSentEvent
			.extracting(this::getReason).contains(Optional.of("Search query returned zero results"));
	}

	@Test
	@DisplayName("ESRF request with multiple results does not send notification")
	void testCreate_nonUniqueSearchResults() {
		// the email search must return multiple statuses; the values inside these statuses don't matter for this test
		final var searchResults = List.<PassportStatus> of(ImmutablePassportStatus.builder().build(), ImmutablePassportStatus.builder().build());
		when(passportStatusService.emailSearch(any(), any(), any(), any())).thenReturn(searchResults);

		// fire the target controller method!
		controller.create(electronicServiceRequest);

		// verify that no notifications were sent
		verifyNoInteractions(notificationService);

		// verify the correct number of events were fired, capturing the events in the process
		final var eventCaptor = ArgumentCaptor.forClass(Object.class);
		verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

		assertThat(eventCaptor).extracting(ArgumentCaptor::getAllValues).asInstanceOf(LIST)
			// verify that the correct event types were fired
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationRequestedEvent.class, event))
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationNotSentEvent.class, event))
			// verify the reason string in the NotificationNotSentEvent
			.extracting(this::getReason).contains(Optional.of("Search query returned non-unique results"));
	}

	@Test
	@DisplayName("ESRF request with single result sends notification")
	void testCreate_uniqueSearchResults() {
		// the email search must return a single status
		final var searchResults = List.<PassportStatus> of(ImmutablePassportStatus.builder().fileNumber("FILE0000").givenName("John").surname("Doe").build());
		when(passportStatusService.emailSearch(any(), any(), any(), any())).thenReturn(searchResults);

		// fire the target controller method!
		controller.create(electronicServiceRequest);

		// verify that the notification service was fired
		verify(notificationService).sendFileNumberNotification(any(), any());

		// verify the correct number of events were fired, capturing the events in the process
		final var eventCaptor = ArgumentCaptor.forClass(Object.class);
		verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

		// verify that the correct event types were fired
		assertThat(eventCaptor).extracting(ArgumentCaptor::getAllValues).asInstanceOf(LIST)
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationRequestedEvent.class, event))
			.anyMatch(event -> ClassUtils.isAssignableValue(NotificationSentEvent.class, event));
	}

	/**
	 * Helper method to extract a reason string from a NotificationNotSentEvent.
	 */
	Optional<String> getReason(Object object) {
		return ClassUtils.isAssignableValue(NotificationNotSentEvent.class, object)
			? Optional.ofNullable(((NotificationNotSentEvent) object).getReason())
			: Optional.empty();
	}

}
