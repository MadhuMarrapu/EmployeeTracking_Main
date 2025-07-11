package com.qentelli.employeetrackingsystem.models.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResourceRequest {

	private Integer resourceId;
	private int Onsite;
	private int Offsite;
}
