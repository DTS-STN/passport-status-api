package ca.gov.dtsstn.passport.api.config;

import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.to;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toLinks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ca.gov.dtsstn.passport.api.config.properties.ApplicationProperties;
import ca.gov.dtsstn.passport.api.security.JwtGrantedAuthoritiesConverter;
import ca.gov.dtsstn.passport.api.web.AuthenticationErrorHandler;
import ca.gov.dtsstn.passport.api.web.ChangelogEndpoint;

/**
 * @author Greg Baker <gregory.j.baker@hrsdc-rhdcc.gc.ca>
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired ApplicationProperties applicationProperties;

	/**
	 * CORS configuration bean.
	 */
	@Bean CorsConfiguration corsConfiguration() {
		log.info("Creating 'corsConfiguration' bean");
		final var corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedHeaders(applicationProperties.security().cors().allowedHeaders());
		corsConfiguration.setAllowedMethods(applicationProperties.security().cors().allowedMethods());
		corsConfiguration.setAllowedOrigins(applicationProperties.security().cors().allowedOrigins());
		corsConfiguration.setExposedHeaders(applicationProperties.security().cors().exposedHeaders());
		return corsConfiguration;
	}

	@Bean CorsConfigurationSource corsConfigurationSource() {
		log.info("Creating 'corsConfigurationSource' bean");
		final var corsConfiguration = corsConfiguration();
		final var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
		corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return corsConfigurationSource;
	}

	@Bean SecurityFilterChain securityFilterChain(AuthenticationErrorHandler authenticationErrorController, Environment environment, HttpSecurity http, JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) throws Exception {
		log.info("Configuring Spring Security");

		final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		http // general security configuration
			.csrf().disable()
			.cors().and()
			.exceptionHandling()
				.accessDeniedHandler(authenticationErrorController).and()
			.headers()
				.cacheControl().disable()
				.contentSecurityPolicy(applicationProperties.security().contentSecurityPolicy().toString()).and()
				.frameOptions().sameOrigin()
				.referrerPolicy(ReferrerPolicy.NO_REFERRER).and().and()
			.oauth2ResourceServer()
				.authenticationEntryPoint(authenticationErrorController)
				.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter).and().and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http // public resources
			.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.requestMatchers(toLinks()).permitAll()
				.requestMatchers(to(ChangelogEndpoint.class)).permitAll()
				.requestMatchers(to(HealthEndpoint.class)).permitAll()
				.requestMatchers(to(InfoEndpoint.class)).permitAll();

		http // protected resources
			.authorizeRequests()
				.requestMatchers(toAnyEndpoint()).authenticated();

		return http.build();
	}

	@Bean WebSecurityCustomizer webSecurityCustomizer() {
		log.debug("Adding /h2-console/** to Spring Security ignore list");
		return web -> web.ignoring().antMatchers("/h2-console/**");
	}

}
