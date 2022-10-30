package ca.gov.dtsstn.passport.api.web;

import java.util.List;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonView;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;

/**
 * A simple {@link RestControllerAdvice} that will apply a {@link JsonView} by inspecting the current user's authorities.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@RestControllerAdvice
public class SecurityController extends AbstractMappingJacksonResponseBodyAdvice {

	@Override
	protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
		bodyContainer.setSerializationView(Authorities.AnonymousView.class);

		if (isAuthenticated(SecurityContextHolder.getContext().getAuthentication())) {
			bodyContainer.setSerializationView(Authorities.AuthenticatedView.class);
		}
	}

	protected boolean isAuthenticated(@Nullable Authentication authentication) {
		return authentication != null && getAuthorities(authentication).stream().noneMatch("ROLE_ANONYMOUS"::equals);
	}

	protected List<String> getAuthorities(@Nullable Authentication authentication) {
		final var grantedAuthorities = Optional.ofNullable(authentication).map(Authentication::getAuthorities).orElse(List.of());
		return grantedAuthorities.stream().map(GrantedAuthority::getAuthority).toList();
	}

}
