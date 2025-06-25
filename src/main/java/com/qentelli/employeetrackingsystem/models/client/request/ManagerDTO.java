package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.entity.TechStack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ManagerDTO {
    private Integer managerId;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;
    private String password;
    private String confirmPassword;
    private Roles role;
    private List<Integer> projectIds;
    private TechStack techStack;
}