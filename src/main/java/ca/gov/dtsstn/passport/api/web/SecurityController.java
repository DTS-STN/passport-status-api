package ca.gov.dtsstn.passport.api.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonView;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;

/**
 * A simple {@link RestControllerAdvice} that will apply a {@link JsonView} by inspecting the current user's authorities.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice(basePackages = "ca.gov.dtsstn.passport.api.web")
public class SecurityController implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return JacksonJsonHttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		return body;
	}

	@Override
	public Map<String, Object> determineWriteHints(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType) {
		final var view = isAuthenticated(SecurityContextHolder.getContext().getAuthentication())
				? Authorities.AuthenticatedView.class
				: Authorities.AnonymousView.class;
		return Map.of(JsonView.class.getName(), view);
	}

	protected boolean isAuthenticated(@Nullable Authentication authentication) {
		return authentication != null && getAuthorities(authentication).stream().noneMatch("ROLE_ANONYMOUS"::equals);
	}

	protected List<String> getAuthorities(@Nullable Authentication authentication) {
		final var grantedAuthorities = Optional.ofNullable(authentication).map(Authentication::getAuthorities).orElse(List.of());
		return grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
	}

}
