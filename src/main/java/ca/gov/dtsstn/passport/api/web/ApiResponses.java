package ca.gov.dtsstn.passport.api.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.gov.dtsstn.passport.api.config.SpringDocConfig.ExampleRefs;
import ca.gov.dtsstn.passport.api.web.model.ErrorResponseModel;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Holder interface for common {@link ApiResponse} meta-annotations.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface ApiResponses {

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@ApiResponse(responseCode = "400", description = "Returned if any of the request parameters are not valid.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.BAD_REQUEST_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface BadRequestError {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@ApiResponse(responseCode = "401", description = "Returned if the request lacks valid authentication credentials for the requested resource.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.AUTHENTICATION_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface AuthenticationError {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@ApiResponse(responseCode = "403", description = "Returned if the the server understands the request but refuses to authorize it.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.ACCESS_DENIED_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface AccessDeniedError {}

	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiResponse(responseCode = "404", description = "Returned if resource was not found or the user does not have access to the resource.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.RESOURCE_NOT_FOUND_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface ResourceNotFoundError {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@ApiResponse(responseCode = "422", description = "Returned if uniqueness was requested but the search query returned non-unique results.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.UNPROCESSABLE_ENTITY_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface UnprocessableEntityError {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@ApiResponse(responseCode = "500", description = "Returned when an unexpected error has occurred.", content = { @Content(examples = { @ExampleObject(name = "Default", ref = ExampleRefs.INTERNAL_SERVER_ERROR) }, schema = @Schema(implementation = ErrorResponseModel.class)) })
	public @interface InternalServerError {}

}
