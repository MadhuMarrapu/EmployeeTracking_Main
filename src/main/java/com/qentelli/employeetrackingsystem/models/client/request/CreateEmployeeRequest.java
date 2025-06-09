package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private List<Integer> projectIds;
    private List<Integer> techStackIds;
    private String role; // Should be EMPLOYEE, MANAGER, or SUPERADMIN
}
