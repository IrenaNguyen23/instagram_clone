package com.instagram.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class AppConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); 
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*")); 
                    config.setExposedHeaders(Arrays.asList("Authorization"));
                    config.setAllowCredentials(true); 
                    return config;
                })
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/signup").permitAll() 
                .anyRequest().authenticated() // All other requests need authentication
                .and()
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class) 
                .addFilterBefore(new JwtTokenValidationFilter(), BasicAuthenticationFilter.class)
                .csrf().disable()
                .formLogin().and().httpBasic(); 

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
