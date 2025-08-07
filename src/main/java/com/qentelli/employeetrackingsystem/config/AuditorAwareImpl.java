package com.qentelli.employeetrackingsystem.config;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	private final Map<String, Map<String, String>> adminMetadata;
	public AuditorAwareImpl(Map<String, Map<String, String>> adminMetadata) {
		this.adminMetadata = adminMetadata;
	}

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
			return Optional.of("System");
		}
		String username = auth.getName();
		Map<String, String> meta = adminMetadata.get(username);
		if (meta != null) {
			String fullName = meta.getOrDefault("firstName", "") + " " + meta.getOrDefault("lastName", "");
			return Optional.of(fullName.trim());
		}
		return Optional.of(username);
	}
}
