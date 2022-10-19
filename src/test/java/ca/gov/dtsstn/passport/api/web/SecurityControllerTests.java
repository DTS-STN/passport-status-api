package ca.gov.dtsstn.passport.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import ca.gov.dtsstn.passport.api.web.annotation.Authorities;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class SecurityControllerTests {

	SecurityController securityController;

	@BeforeEach
	void beforeEach() {
		this.securityController = new SecurityController();
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("beforeBodyWriteInternal(..) correctly returns anonymous view")
	void testBeforeBodyWriteInternal_anonymous() {
		final var authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
		final var authentication = new AnonymousAuthenticationToken("key", "anonymous", authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final var bodyContainer = mock(MappingJacksonValue.class);
		final var contentType = mock(MediaType.class);
		final var returnType = mock(MethodParameter.class);
		final var request = mock(ServerHttpRequest.class);
		final var response = mock(ServerHttpResponse.class);

		securityController.beforeBodyWriteInternal(bodyContainer, contentType, returnType, request, response);

		verify(bodyContainer).setSerializationView(Authorities.AnonymousView.class);
		verify(bodyContainer, never()).setSerializationView(Authorities.AuthenticatedView.class);
	}

	@Test
	@DisplayName("beforeBodyWriteInternal(..) correctly returns authenticated view")
	void testBeforeBodyWriteInternal_authenticated() {
		final var authorities = AuthorityUtils.createAuthorityList("Application.Manage", "Users.Read.All");
		final var authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final var bodyContainer = mock(MappingJacksonValue.class);
		final var contentType = mock(MediaType.class);
		final var returnType = mock(MethodParameter.class);
		final var request = mock(ServerHttpRequest.class);
		final var response = mock(ServerHttpResponse.class);

		securityController.beforeBodyWriteInternal(bodyContainer, contentType, returnType, request, response);

		verify(bodyContainer).setSerializationView(Authorities.AuthenticatedView.class);
	}

	@Test
	@DisplayName("getAuthorities(Application.Manage, Users.Read.All) returns correct authorities")
	void testGetAuthorities() {
		final var authorities = AuthorityUtils.createAuthorityList("Application.Manage", "Users.Read.All");
		final var authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
		assertThat(securityController.getAuthorities(authentication)).containsExactly("Application.Manage", "Users.Read.All");
	}

	@Test
	@DisplayName("isAuthenticated(null) returns false")
	void testIsAuthenticated_nullAuthentication() {
		assertThat(securityController.isAuthenticated(null)).isFalse();
	}

	@Test
	@DisplayName("isAuthenticated(anonymousUser) returns false")
	void testIsAuthenticated_anonymousAuthentication() {
		final var authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
		final var authentication = new AnonymousAuthenticationToken("key", "anonymous", authorities);
		assertThat(securityController.isAuthenticated(authentication)).isFalse();
	}

	@Test
	@DisplayName("isAuthenticated(user) returns true")
	void testIsAuthenticated_userAuthentication() {
		final var authorities = AuthorityUtils.createAuthorityList("Application.Manage");
		final var authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
		assertThat(securityController.isAuthenticated(authentication)).isTrue();
	}

}
