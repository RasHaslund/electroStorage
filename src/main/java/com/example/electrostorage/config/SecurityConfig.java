package com.example.electrostorage.config;

import com.example.electrostorage.utils.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/login.html", "/*.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/components/**", "/suppliers/**", "/assemblies/**", "/orders/**", "/inventory/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/assemblies/*/produce").permitAll()
                        .requestMatchers("/orders/**", "/inventory/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/components/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/components/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/suppliers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/suppliers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/assemblies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/assemblies/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
