package fr.inra.urgi.gpds.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Security configuration. It makes sure that harvest endpoints and the actuator endpoints are only accessible to
 * authenticated users
 *
 * @author JB Nizet
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                //.antMatchers("/api/harvests", "/api/harvests/**").authenticated()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated() // EndpointRequest.toAnyEndpoint()
                                                                                  // only targets the **actuator**
                                                                                  // endpoints.
                .antMatchers("/**").permitAll()
            .and()
                .httpBasic()
            .and()
                .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
