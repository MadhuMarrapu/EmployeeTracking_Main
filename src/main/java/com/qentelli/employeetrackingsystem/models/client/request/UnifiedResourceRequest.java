package com.qentelli.employeetrackingsystem.models.client.request;

import com.qentelli.employeetrackingsystem.entity.ResourceType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnifiedResourceRequest {

    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    private TechStackResourceRequest techStackRequest;
    private ProjectResourceRequest projectRequest;
}