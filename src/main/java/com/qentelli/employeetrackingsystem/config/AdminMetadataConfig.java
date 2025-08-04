package com.qentelli.employeetrackingsystem.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminMetadataConfig {

	@Bean
	public Map<String, Map<String, String>> adminMetadata() {
		Map<String, Map<String, String>> details = new HashMap<>();

		details.put("admin1@example.com", Map.of("firstName", "Amit", "lastName", "Kumar"));
		details.put("admin2@example.com", Map.of("firstName", "Neha", "lastName", "Verma"));
		details.put("admin3@example.com", Map.of("firstName", "Raj", "lastName", "Singh"));

		return details;
	}
}