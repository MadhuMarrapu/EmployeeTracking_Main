package com.qentelli.employeetrackingsystem.models.client.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectDetailsDto {

	private String projectName;
	private String location;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean action;

}
