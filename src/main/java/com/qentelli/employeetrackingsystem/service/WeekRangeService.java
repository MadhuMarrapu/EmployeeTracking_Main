package com.qentelli.employeetrackingsystem.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;

public interface WeekRangeService {

	public void saveWeeklyData(WeekRangeRequest request);
	public Page<WeekRangeResponse> generateReport(LocalDate weekFromDate, LocalDate weekToDate, Pageable pageable);
	public WeekRangeResponse getById(int id);
	public void softDelete(int id);
	public boolean setWeekRangeEnabled(Integer id);
}