package ca.gov.dtsstn.passport.api.web;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.web.model.ImmutableErrorResponseModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableIssueModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeStatusModel;

/**
 * This class functions as both a {@code RestControllerAdvice}, as well as a
 * Spring Security {@code AccessDeniedHandler} and
 * {@code AuthenticationEntryPoint}.
 * <p>
 * The reason for the dual-responsibility is because of how Spring Security
 * handles rules configured in its
 * {@code SecurityFilterChain} vs {@code @PreAuthorize} annotated methods. The
 * former are handled as
 * {@code AccessDeniedHandler} and {@code AuthenticationEntryPoint}, the latter
 * as an {@code @ExceptionHandler}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // this should fire before ApiErrorController
public class AuthenticationErrorHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationErrorHandler.class);

	private final ObjectMapper objectMapper;

	public AuthenticationErrorHandler(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper is required; it must not be null");
		this.objectMapper = objectMapper;
	}

	@Override
	@ExceptionHandler({ AuthenticationException.class })
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		// The only authentication we have is from Interop, so this is a canary log
		// statement for our analytics teams, where if this happens and the IP isn't on
		// our expected subnet we'll know something's going wrong within the VNET or
		// wider hub/spoke.
		log.error("Authentication Error: Code = 401, Remote Address = " + request.getRemoteAddr());

		final var body = ImmutableErrorResponseModel.builder()
				.operationOutcome(ImmutableOperationOutcomeModel.builder()
						.addIssues(ImmutableIssueModel.builder()
								.issueCode("API-0401")
								.issueDetails(
										"The request lacks valid authentication credentials for the requested resource.")
								.build())
						.operationOutcomeStatus(ImmutableOperationOutcomeStatusModel.builder()
								.statusCode("401")
								.statusDescriptionText("Unauthorized")
								.build())
						.build())
				.build();

		sendResponse(response, HttpStatus.UNAUTHORIZED, body);
	}

	@Override
	@ExceptionHandler({ AccessDeniedException.class })
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		if (isAnonymous()) {
			// Spring Security has this odd quirk whereby it will not throw an
			// AuthenticationCredentialsNotFoundException if the current user is an
			// anonymous user. 🤷
			// see: AbstractSecurityInterceptor.beforeInvocation(..) for reference
			throw new AuthenticationCredentialsNotFoundException(accessDeniedException.getMessage());
		}

		// The only authentication we have is from Interop, so this is a canary log
		// statement for our analytics teams, where if this happens and the IP isn't on
		// our expected subnet we'll know something's going wrong within the VNET or
		// wider hub/spoke.
		log.error("Authentication Error: Code = 403, Remote Address = " + request.getRemoteAddr());

		final var body = ImmutableErrorResponseModel.builder()
				.operationOutcome(ImmutableOperationOutcomeModel.builder()
						.addIssues(ImmutableIssueModel.builder()
								.issueCode("API-0403")
								.issueDetails("The server understands the request but refuses to authorize it.")
								.build())
						.operationOutcomeStatus(ImmutableOperationOutcomeStatusModel.builder()
								.statusCode("403")
								.statusDescriptionText("Forbidden")
								.build())
						.build())
				.build();

		sendResponse(response, HttpStatus.FORBIDDEN, body);
	}

	protected void sendResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws IOException {
		if (response.isCommitted()) {
			log.warn("Response already committed");
			return;
		}

		response.setStatus(httpStatus.value());
		response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(body));
	}

	protected boolean isAnonymous() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
				.map(Authentication::getAuthorities).orElse(Collections.emptyList()).stream()
				.map(GrantedAuthority::getAuthority).anyMatch("ROLE_ANONYMOUS"::equals);
	}

}
