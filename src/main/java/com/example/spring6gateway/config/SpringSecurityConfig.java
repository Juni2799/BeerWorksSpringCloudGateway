package com.example.spring6gateway.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SpringSecurityConfig {

    //http://localhost:8080/actuator/health
    @Bean
    @Order(1)
    public SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeExchange(authorize -> authorize.anyExchange().permitAll());

        return http.build();
    }

    //Below code is modified to allow authencation via both, Http Basic and OAuth2.
    @Bean
    @Order(2)
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/v1/**").authenticated()
                        .pathMatchers("/api/v2/**", "/api/v3/**").authenticated()
                        .anyExchange().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    //Below Bean is needed for Http Basic auth since the call httpBasic() needs either a ReactiveAuthenticationManager or ReactiveUserDetailsService.
    //If neither is available, "authenticationManager cannot be null" is thrown hence we have created a bean of MapReactiveUserDetailsService
    //which in turn will provide a ReactiveAuthenticationManager for http basic auth during app boot up.
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
                .withUsername("user1")
                .password("{noop}password") // Replace with password encoder for production
                //.roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
