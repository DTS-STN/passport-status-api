package ca.gov.dtsstn.passport.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonView;

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
	@DisplayName("determineWriteHints(..) correctly returns anonymous view")
	void testDetermineWriteHints_anonymous() {
		final var authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
		final var authentication = new AnonymousAuthenticationToken("key", "anonymous", authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final var returnType = mock(MethodParameter.class);
		final var hints = securityController.determineWriteHints(null, returnType, MediaType.APPLICATION_JSON,
				JacksonJsonHttpMessageConverter.class);

		assertThat(hints).containsEntry(JsonView.class.getName(), Authorities.AnonymousView.class);
	}

	@Test
	@DisplayName("determineWriteHints(..) correctly returns authenticated view")
	void testDetermineWriteHints_authenticated() {
		final var authorities = AuthorityUtils.createAuthorityList("Application.Manage", "Users.Read.All");
		final var authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		final var returnType = mock(MethodParameter.class);
		final var hints = securityController.determineWriteHints(null, returnType, MediaType.APPLICATION_JSON,
				JacksonJsonHttpMessageConverter.class);

		assertThat(hints).containsEntry(JsonView.class.getName(), Authorities.AuthenticatedView.class);
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
