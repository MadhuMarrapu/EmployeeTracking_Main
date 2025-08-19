package com.qentelli.employeetrackingsystem.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;

public interface WeekRangeService {

	void saveWeeklyData(WeekRangeRequest request);

	Page<WeekRangeResponse> generateReport(LocalDate weekFromDate, LocalDate weekToDate, Pageable pageable);

	WeekRangeResponse getById(int id);

	void softDelete(int id);

	boolean setWeekRangeEnabled(Integer id);
}