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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    private static OrRequestMatcher apiRequestMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**"),
            new AntPathRequestMatcher("/actuator/**"));

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

    @Bean SecurityFilterChain apiSecurityFilterChain(AuthenticationErrorHandler authenticationErrorController,
            Environment environment, HttpSecurity http, JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter)
            throws Exception {
        log.info("Configuring API Security");

        http.requestMatcher(apiRequestMatcher)
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(toLinks()).permitAll()
                        .requestMatchers(to(ChangelogEndpoint.class)).permitAll()
                        .requestMatchers(to(HealthEndpoint.class)).permitAll()
                        .requestMatchers(to(InfoEndpoint.class)).permitAll()
                        .requestMatchers(toAnyEndpoint()).hasAuthority("Application.Manage"));

        return commonFilterChain(authenticationErrorController, environment, http, jwtGrantedAuthoritiesConverter);
    }

    @Bean SecurityFilterChain webSecurityFilterChain(AuthenticationErrorHandler authenticationErrorController,
            Environment environment, HttpSecurity http, JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter)
            throws Exception {
        log.info("Configuring web Security");

        http.requestMatcher(new NegatedRequestMatcher(apiRequestMatcher)).headers()
                .contentSecurityPolicy(applicationProperties.security().contentSecurityPolicy().toString()).and()
                .frameOptions().sameOrigin()
                .referrerPolicy(ReferrerPolicy.NO_REFERRER);

        return commonFilterChain(authenticationErrorController, environment, http, jwtGrantedAuthoritiesConverter);
    }

    SecurityFilterChain commonFilterChain(AuthenticationErrorHandler authenticationErrorController,
            Environment environment, HttpSecurity http, JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter)
            throws Exception {
        log.info("Configuring Spring Security");

        final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        http.cors().and()
                .exceptionHandling()
                .accessDeniedHandler(authenticationErrorController).and()
                .oauth2ResourceServer()
                .authenticationEntryPoint(authenticationErrorController)
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter).and().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean WebSecurityCustomizer webSecurityCustomizer() {
        log.debug("Adding /h2-console/** to Spring Security ignore list");
        return web -> web.ignoring().antMatchers("/h2-console/**");
    }

}
