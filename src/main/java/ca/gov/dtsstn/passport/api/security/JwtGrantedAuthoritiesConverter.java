package ca.gov.dtsstn.passport.api.security;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Component that will convert a JWT to a collection of {@link GrantedAuthority}
 * instances.
 * <p>
 * Sample claim from Azure Active Directory:
 *
 * <pre>
 * 	{
 * 	  roles: [
 * 	    "CPP.Manage",
 * 	    "CPP.SomeOtherRole"
 * 	  ]
 * 	}
 * </pre>
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @since 0.0.0
 */
@Component
@ConfigurationProperties("application.jwt-granted-authorities-converter")
public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private String rolesClaim = "roles";

	@Override
	public List<GrantedAuthority> convert(Jwt jwt) {
		return Optional.ofNullable(jwt.getClaimAsStringList(rolesClaim))
			.orElse(Collections.emptyList()).stream()
			.map(SimpleGrantedAuthority::new)
			.collect(toList());
	}

	public void setRolesClaim(String rolesClaim) {
		Assert.hasText(rolesClaim, "rolesClaim is required; it must not be null or blank");
		this.rolesClaim = rolesClaim;
	}

}
