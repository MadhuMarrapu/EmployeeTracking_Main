package com.qentelli.employeetrackingsystem.models.client.request;

//import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectDetailsDto {

	private String projectName;
	private String location;
	//private LocalDate startDate;
	//private LocalDate endDate;
	//private Boolean action;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;

}
