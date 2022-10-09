package ca.gov.dtsstn.passport.api.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class JwtGrantedAuthoritiesConverterTests {

	JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

	@BeforeEach void beforeEach() {
		this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		this.jwtGrantedAuthoritiesConverter.setRolesClaim("userRoles");
	}

	@Test
	void testConvert() {
		final var jwtx = mock(Jwt.class);

		when(jwtx.getClaimAsStringList("userRoles")).thenReturn(List.of("Administrator", "User"));

		assertThat(jwtGrantedAuthoritiesConverter.convert(jwtx)).extracting(GrantedAuthority::getAuthority).containsExactly("Administrator", "User");
	}

}
