package com.qentelli.employeetrackingsystem.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import com.qentelli.employeetrackingsystem.serviceImpl.PersonDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private PersonDetailService personDetailService;

	// 👤 In-memory admin users
	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		UserDetails admin1 = User.withUsername("superadmin@gmail.com").password(passwordEncoder().encode("Sarath11@"))
				.roles("SUPERADMIN").build();

		UserDetails admin2 = User.withUsername("superadmin2@gmail.com").password(passwordEncoder().encode("Madhu123@"))
				.roles("SUPERADMIN").build();

		UserDetails admin3 = User.withUsername("Anil@qentelli.com").password(passwordEncoder().encode("Anil123@"))
				.roles("SUPERADMIN").build();

		return new InMemoryUserDetailsManager(admin1, admin2, admin3);
	}

	@Bean(name = "userDetailsService")
	public UserDetailsService userDetailsService(InMemoryUserDetailsManager inMemoryManager,
			PersonDetailService personService) {

		return username -> {
			try {
				return inMemoryManager.loadUserByUsername(username);
			} catch (UsernameNotFoundException e) {
				return personService.loadUserByUsername(username);
			}
		};
	}

	// 🔐 AuthenticationManager with custom provider
	@Bean
	public AuthenticationManager authenticationManager(@Qualifier("userDetailsService") UserDetailsService uds) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(provider);
	}

	// 🔓 Filter chain config
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/auth/register", "/auth/login").permitAll().anyRequest().authenticated())
				.userDetailsService(userDetailsService(inMemoryUserDetailsManager(), personDetailService))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// 🔒 Password encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 🌐 CORS config
	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:4200")); // Adjust as needed
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}