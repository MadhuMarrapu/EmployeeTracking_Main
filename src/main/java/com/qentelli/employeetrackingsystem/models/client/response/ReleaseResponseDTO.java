package com.qentelli.employeetrackingsystem.models.client.response;

import com.qentelli.employeetrackingsystem.entity.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseResponseDTO {

    private String projectName;
    private int major;
    private int minor;
    private int incidentCreated;
    private String releaseInformation;
    private int weekId;
    private long sprintId;
    private long releaseId;
    private Integer projectId;
    private Status statusFlag; // ✅ Lifecycle state
}