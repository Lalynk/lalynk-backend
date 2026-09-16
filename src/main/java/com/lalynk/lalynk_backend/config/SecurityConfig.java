package com.lalynk.lalynk_backend.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http.cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .authorizeHttpRequests(authorize -> authorize.requestMatchers
                    ("/secrets/public/**",
                            "/auth/me",
                            "/auth/login",
                            "/auth/csrf").permitAll()
                    .anyRequest().authenticated())


            .oauth2Login(oauth2 ->
                    oauth2.defaultSuccessUrl("http://localhost:5173", true));

    return http.build();
}

@Bean
    public GrantedAuthoritiesMapper useAuthoritiesMapper() {
        return new AuthoritiesMapper();
}




}
