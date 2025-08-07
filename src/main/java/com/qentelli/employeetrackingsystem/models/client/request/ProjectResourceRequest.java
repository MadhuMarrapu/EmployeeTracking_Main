package com.qentelli.employeetrackingsystem.models.client.request;

import com.qentelli.employeetrackingsystem.entity.ResourceType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectResourceRequest implements BaseResourceRequest {

    @NotNull(message = "Resource type must be PROJECT")
    private final ResourceType resourceType = ResourceType.PROJECT;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    private int onsite;
    private int offsite;
    private Boolean resourceStatus = true;
}