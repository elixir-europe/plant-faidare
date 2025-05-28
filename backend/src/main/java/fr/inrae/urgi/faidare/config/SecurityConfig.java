package fr.inrae.urgi.faidare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration. It makes sure the actuator endpoints are only accessible to
 * authenticated users
 *
 * @author JB Nizet
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(
                registry -> registry.requestMatchers(AntPathRequestMatcher.antMatcher("/actuator**")).authenticated()
            )
            .httpBasic(configurer -> {})
            .csrf(configurer -> configurer.disable())
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    public WebSecurityCustomizer ignoringActuatorWebSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(RegexRequestMatcher.regexMatcher("^((?!\\/actuator).)*$"));
    }
}
