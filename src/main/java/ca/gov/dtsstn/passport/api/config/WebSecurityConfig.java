package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
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
@EnableMethodSecurity
public class WebSecurityConfig {

	private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired ApplicationProperties applicationProperties;

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


	/**
	 * Security configuration for actuator and API endpoints.
	 */
	@Bean SecurityFilterChain apiSecurityFilterChain(AuthenticationErrorHandler authenticationErrorController, Environment environment, HttpSecurity http, JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) throws Exception {
		log.info("Configuring API security");

		final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		final var actuatorRequest = EndpointRequest.toAnyEndpoint();
		final var apiRequest = AntPathRequestMatcher.antMatcher("/api/**");

		http.securityMatcher(new OrRequestMatcher(actuatorRequest, apiRequest))
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(authenticationErrorController))
			.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
				.authenticationEntryPoint(authenticationErrorController)
				.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
			// allow XHR preflight checks
			.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS)).permitAll()

			/*
			 * actuator requests
			 */
			.requestMatchers(EndpointRequest.toLinks()).permitAll()
			.requestMatchers(EndpointRequest.to(ChangelogEndpoint.class)).permitAll()
			.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
			.requestMatchers(EndpointRequest.to(InfoEndpoint.class)).permitAll()
			.requestMatchers(actuatorRequest).hasAuthority(environment.getProperty("management.authorized-role"))

			/*
			 * API requests (using permitAll() because these are to be secured with @PreAuthorize)
			 */
			.requestMatchers(apiRequest).permitAll()

			// lock 'er down
			.anyRequest().denyAll());

		return http.build();
	}

	/**
	 * Security configuration for non-actuator and non-API endpoints (ex: h2-console and swagger).
	 */
	@Bean SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		log.info("Configuring non-API web security");

		final var policyDirectives = applicationProperties.security().contentSecurityPolicy().toString();
		final var openApiRequest = new OrRequestMatcher(AntPathRequestMatcher.antMatcher("/swagger-ui/**"), AntPathRequestMatcher.antMatcher("/v3/api-docs/**"));

		http
			.headers(headers -> headers
				.contentSecurityPolicy(contentSecurityPolicy -> contentSecurityPolicy.policyDirectives(policyDirectives))
				.frameOptions(frameOptions -> frameOptions.sameOrigin())
				.referrerPolicy(referrerPolicy -> referrerPolicy.policy(ReferrerPolicy.NO_REFERRER)))
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
			.requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/error")).permitAll()
			.requestMatchers(openApiRequest).permitAll()
			.anyRequest().denyAll());

		return http.build();
	}

	@Bean WebSecurityCustomizer webSecurityCustomizer() {
		log.info("Adding /h2-console/** to Spring Security ignore list");
		return web -> web.ignoring().requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"));
	}
}
