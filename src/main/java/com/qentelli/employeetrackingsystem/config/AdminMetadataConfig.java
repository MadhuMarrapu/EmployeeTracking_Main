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

		details.put("superadmin@gmail.com", Map.of("firstName", "Sarath", "lastName", "Tenneti"));
		details.put("superadmin2@gmail.com", Map.of("firstName", "Madhu", "lastName", "Marrapu"));
		details.put("Anil@qentelli.com", Map.of("firstName", "Anil", "lastName", "Kumar"));

		return details;
	}
}