package com.lalynk.lalynk_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http.cors(Customizer.withDefaults()).authorizeHttpRequests(authorize ->
            authorize.requestMatchers("/secrets/public/**", "/auth/me", "/auth/login").permitAll().anyRequest().authenticated())
            .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:5173"));

    return http.build();
}









}
