package com.qentelli.employeetrackingsystem.models.client.request;

import java.util.List;

import com.qentelli.employeetrackingsystem.entity.TechStack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private TechStack techStack;
    private List<Integer> projectIds;

    // Getters and Setters
}