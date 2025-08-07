package com.qentelli.employeetrackingsystem.models.client.request;

import com.qentelli.employeetrackingsystem.entity.ResourceType;
import com.qentelli.employeetrackingsystem.entity.TechStack;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TechStackResourceRequest implements BaseResourceRequest {

    @NotNull(message = "Resource type must be TECH_STACK")
    private final ResourceType resourceType = ResourceType.TECH_STACK;

    @NotNull(message = "Tech stack is required")
    private TechStack techStack;

    private int onsite;
    private int offsite;
    private Boolean resourceStatus = true;
}