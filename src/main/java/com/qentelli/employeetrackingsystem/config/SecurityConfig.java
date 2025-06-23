package com.qentelli.employeetrackingsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow any origin (can restrict to specific domains)
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
      //  config.addAllowedOrigin("http://localhost:4200");  // Frontend URL (Angular's default)
        
        // Allow headers and methods
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        
        // Apply CORS to all paths
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Security Filter Chain Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure Spring Security HTTP settings
        http
                .cors(cors -> cors // Apply CORS configuration
                        .configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection (needed for stateless JWT)
                .authorizeHttpRequests(auth -> auth
                              //  .requestMatchers("/auth/**", "/api/**")
                                .requestMatchers("/auth/register", "/auth/login")
                                .permitAll()  // Allow authentication and API paths
                                .anyRequest().authenticated()  // Require authentication for other requests
                )
                .userDetailsService(userDetailsService)  // Set custom user details service (for authentication)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before the authentication filter
        
        return http.build();  // Build and return the security configuration
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
