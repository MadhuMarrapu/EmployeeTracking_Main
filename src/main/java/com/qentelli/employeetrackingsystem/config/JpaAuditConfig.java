package com.qentelli.employeetrackingsystem.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
public class JpaAuditConfig {

    private final Map<String, Map<String, String>> adminMetadata;
    public JpaAuditConfig(Map<String, Map<String, String>> adminMetadata) {
        this.adminMetadata = adminMetadata;
    }
    @Bean
    public AuditorAware<String> auditorAwareImpl() {
        return new AuditorAwareImpl(adminMetadata);
    }
}
