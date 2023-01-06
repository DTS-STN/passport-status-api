package ca.gov.dtsstn.passport.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
public class MultiHttpSecurityConfig {

    @Bean
    // @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        OrRequestMatcher apiRequestMatcher = new OrRequestMatcher(
        new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**")),
        new NegatedRequestMatcher(new AntPathRequestMatcher("/actuator/**")));

        http.requestMatcher(apiRequestMatcher).headers()
                .contentSecurityPolicy("'dummyHeader','dummyValue'");
        return http.build();
    }
}