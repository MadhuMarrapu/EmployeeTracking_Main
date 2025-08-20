package com.qentelli.employeetrackingsystem.models.client.request;

import com.qentelli.employeetrackingsystem.entity.enums.ResourceType;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.entity.enums.TechStack;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequest {

    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    private TechStack techStack;
    private Integer projectId;
    private Long sprintId;

    @Min(value = 0, message = "Onsite count must be non-negative")
    private int onsite;

    @Min(value = 0, message = "Offsite count must be non-negative")
    private int offsite;

    private String ratio;

}