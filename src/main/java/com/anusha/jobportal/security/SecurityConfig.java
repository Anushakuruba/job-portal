package com.anusha.jobportal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                    "http://localhost:5500",
                    "http://127.0.0.1:5500"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                    "Authorization",
                    "Content-Type"
                )
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

    	http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(
                corsConfigurationSource()
        ))
        .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/api/test",
                    "/api/users/register",
                    "/api/auth/login"
                    
                ).permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/admin/create"
                ).hasAuthority("ROLE_ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/jobs"
                ).hasAnyAuthority(
                    "ROLE_RECRUITER",
                    "ROLE_ADMIN"
                )

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/jobs/**"
                ).hasAnyAuthority(
                    "ROLE_JOB_SEEKER",
                    "ROLE_RECRUITER",
                    "ROLE_ADMIN"
                )

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/applications"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/applications/my"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/applications/job/**"
                ).hasAnyAuthority(
                    "ROLE_RECRUITER",
                    "ROLE_ADMIN"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/applications/**"
                ).hasAnyAuthority(
                    "ROLE_RECRUITER",
                    "ROLE_ADMIN"
                )
                
                .requestMatchers(
                	    HttpMethod.GET,
                	    "/api/admin/users"
                	).hasAuthority("ROLE_ADMIN")
                
                .requestMatchers(
                	    HttpMethod.PUT,
                	    "/api/admin/users/**"
                	).hasAuthority("ROLE_ADMIN")
                
                .requestMatchers(
                	    HttpMethod.GET,
                	    "/api/admin/jobs"
                	).hasAuthority("ROLE_ADMIN")
                
                .requestMatchers(
                	    HttpMethod.DELETE,
                	    "/api/admin/jobs/**"
                	).hasAuthority("ROLE_ADMIN")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/saved-jobs/**"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/saved-jobs"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/saved-jobs/**"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/applications/dashboard"
                ).hasAuthority("ROLE_JOB_SEEKER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/recruiter/dashboard"
                ).hasAuthority("ROLE_RECRUITER")

                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}