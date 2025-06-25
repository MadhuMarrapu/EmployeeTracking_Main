package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDetailsDto {
	private int id;
	private String firstName;
	private String lastName;
	private String employeeId;
	private String email;
	private String password;
	private String confirmPassword;
	private String roles;
	private String techStack;
	private List<ProjectDetailsDto> projects;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;


}