package com.qentelli.employeetrackingsystem.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.qentelli.employeetrackingsystem.serviceimpl.PersonDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private PersonDetailService personDetailService;

    // 🔐 Composite UserDetailsService: In-Memory + DB fallback
    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryManager = new InMemoryUserDetailsManager(
            User.withUsername("superadmin@gmail.com").password(passwordEncoder().encode("Sarath11@")).roles("SUPERADMIN").build(),
            User.withUsername("superadmin2@gmail.com").password(passwordEncoder().encode("Madhu123@")).roles("SUPERADMIN").build(),
            User.withUsername("Anil@qentelli.com").password(passwordEncoder().encode("Anil123@")).roles("SUPERADMIN").build()
        );

        return username -> {
            try {
                return inMemoryManager.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                return personDetailService.loadUserByUsername(username);
            }
        };
    }

    // 🔐 AuthenticationManager wired with composite service
    @Bean
    public AuthenticationManager authenticationManager(@Qualifier("userDetailsService") UserDetailsService uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    // 🔓 Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Qualifier("userDetailsService") UserDetailsService uds) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .anyRequest().authenticated())
            .userDetailsService(uds)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔐 Password hashing strategy
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌐 CORS configuration
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:4200"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}