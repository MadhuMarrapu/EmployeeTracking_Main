package com.qentelli.employeetrackingsystem.models.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
	private Integer resourceId;
	private int Onsite;
	private int Offsite;
}
