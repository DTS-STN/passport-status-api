package ca.gov.dtsstn.passport.api.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.web.model.error.ImmutableAuthenticationErrorModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class ApiAuthenticationHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

	private static final Logger log = LoggerFactory.getLogger(ApiAuthenticationHandler.class);

	private final ObjectMapper objectMapper;

	public ApiAuthenticationHandler(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper is required; it must not be null");
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		final var error = ImmutableAuthenticationErrorModel.builder()
			.details(HttpStatus.UNAUTHORIZED.getReasonPhrase())
			.errorCode("API-0401")
			.message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
			.build();

		sendResponse(response, HttpStatus.UNAUTHORIZED, error);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		final var error = ImmutableAuthenticationErrorModel.builder()
			.details(HttpStatus.FORBIDDEN.getReasonPhrase())
			.errorCode("API-0403")
			.message(HttpStatus.FORBIDDEN.getReasonPhrase())
			.build();

		sendResponse(response, HttpStatus.FORBIDDEN, error);
	}

	protected void sendResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws IOException {
		if (response.isCommitted()) {
			log.warn("Response already committed");
			return;
		}

		response.setStatus(httpStatus.value());
		response.getWriter().write(objectMapper.writeValueAsString(body));
	}

}
