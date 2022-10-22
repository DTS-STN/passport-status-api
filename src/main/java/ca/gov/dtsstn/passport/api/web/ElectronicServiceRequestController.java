package ca.gov.dtsstn.passport.api.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.gov.dtsstn.passport.api.service.NotificationService;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.exception.NonUniqueResourceException;
import ca.gov.dtsstn.passport.api.web.model.CreateElectronicServiceRequestModel;
import ca.gov.dtsstn.passport.api.web.model.error.BadRequestErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.InternalServerErrorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestController
@RequestMapping({ "/api/v1/electronic-service-requests" })
@Tag(name = "Electronic Service Requests", description = "Electronic Service Request API")
@ApiResponse(responseCode = "500", description = "Internal server error.", content = { @Content(schema = @Schema(implementation = InternalServerErrorModel.class)) })
public class ElectronicServiceRequestController {

	private static final Logger log = LoggerFactory.getLogger(ElectronicServiceRequestController.class);

	private final NotificationService notificationService;

	private final PassportStatusService passportStatusService;

	public ElectronicServiceRequestController(NotificationService notificationService, PassportStatusService passportStatusService) {
		log.info("Creating 'electronicServiceRequestController' bean");

		Assert.notNull(notificationService, "notificationService is required; it must not be null");
		Assert.notNull(passportStatusService, "passportStatusService is required; it must not be null");

		this.notificationService = notificationService;
		this.passportStatusService = passportStatusService;
	}

	@PostMapping({ "" })
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Create a new electronic service request.")
	@ApiResponse(responseCode = "202", description = "The request has been accepted for processing.")
	@ApiResponse(responseCode = "400", description = "Returned if the server cannot or will not process the request due to something that is perceived to be a client error.", content = { @Content(schema = @Schema(implementation = BadRequestErrorModel.class)) })
	public void create(@RequestBody @Validated CreateElectronicServiceRequestModel createElectronicServiceRequest) {
		log.trace("New electronic service request posted for: [{}]", createElectronicServiceRequest);

		final var passportStatuses = passportStatusService.emailSearch(createElectronicServiceRequest.getDateOfBirth(), createElectronicServiceRequest.getEmail(), createElectronicServiceRequest.getFirstName(), createElectronicServiceRequest.getLastName());
		log.debug("Found {} file numbers for email address [{}]", passportStatuses.size(), createElectronicServiceRequest.getEmail());

		if (passportStatuses.size() > 1) {
			log.warn("Search query returned non-unique file numbers: {}", createElectronicServiceRequest);
			throw new NonUniqueResourceException("Search query returned non-unique file numbers");
		}

		passportStatuses.stream().findFirst().ifPresent(notificationService::sendFileNumberNotification);
	}

}
