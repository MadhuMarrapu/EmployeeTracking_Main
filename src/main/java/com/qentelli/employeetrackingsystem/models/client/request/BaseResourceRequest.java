package com.qentelli.employeetrackingsystem.models.client.request;

import com.qentelli.employeetrackingsystem.entity.ResourceType;

public interface BaseResourceRequest {
    ResourceType getResourceType();
    int getOnsite();
    int getOffsite();
    Boolean getResourceStatus();
}