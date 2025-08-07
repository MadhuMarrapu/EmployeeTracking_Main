package com.qentelli.employeetrackingsystem.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminMetadataConfig {
	
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	
	@Bean
	public Map<String, Map<String, String>> adminMetadata() {
		Map<String, Map<String, String>> details = new HashMap<>();
		details.put("superadmin@gmail.com", Map.of(FIRST_NAME, "Sarath", LAST_NAME, "Tenneti"));
		details.put("superadmin2@gmail.com", Map.of(FIRST_NAME, "Madhu", LAST_NAME, "Marrapu"));
		details.put("Anil@qentelli.com", Map.of(FIRST_NAME, "Anil", LAST_NAME, "Kumar"));
		return details;
	}
}
