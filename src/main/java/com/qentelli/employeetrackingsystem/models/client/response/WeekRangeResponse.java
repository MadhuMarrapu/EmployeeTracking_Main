package com.qentelli.employeetrackingsystem.models.client.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekRangeResponse {
	private int id;
    private String rangeLabel;
}
