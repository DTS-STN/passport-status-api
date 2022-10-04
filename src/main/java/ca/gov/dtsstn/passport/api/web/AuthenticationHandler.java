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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.web.model.error.ImmutableAccessDeniedErrorModel;
import ca.gov.dtsstn.passport.api.web.model.error.ImmutableAuthenticationErrorModel;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
public class AuthenticationHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationHandler.class);

	private final ObjectMapper objectMapper;

	public AuthenticationHandler(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper is required; it must not be null");
		this.objectMapper = objectMapper;
	}

	@Override
	@ExceptionHandler({ AuthenticationException.class })
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		sendResponse(response, HttpStatus.UNAUTHORIZED, ImmutableAuthenticationErrorModel.of("Access to this resource is denied: bad credentials."));
	}

	@Override
	@ExceptionHandler({ AccessDeniedException.class })
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		sendResponse(response, HttpStatus.FORBIDDEN, ImmutableAccessDeniedErrorModel.of("Access to this resource is denied: forbidden by security policies."));
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
