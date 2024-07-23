package com.example.GeologicalClasses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	/**
     * Defines the security filter chain for HTTP requests.
     * @param http the HttpSecurity object to configure web based security for specific http requests.
     * @return the SecurityFilterChain that is used to make sure the security configurations are applied.
     * @throws Exception in case of any configuration issues.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }

    /**
     * Defines an in-memory user details service with a single user.
     * @return UserDetailsService that is used to load user-specific data.
     */
    @Bean
    public UserDetailsService userDetailsService() {
    	// Create a user with the username "user", password "password", and role "USER"
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}password")
            .roles("USER")
            .build();
        // Return an in-memory user details manager with the created user.
        return new InMemoryUserDetailsManager(user);
    }
}