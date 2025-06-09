package com.qentelli.employeetrackingsystem.models.client.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> projectNames;
    private List<String> techStackNames;
    private String roleName;
}
