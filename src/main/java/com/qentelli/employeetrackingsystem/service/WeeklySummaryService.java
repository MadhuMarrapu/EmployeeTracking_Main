package com.qentelli.employeetrackingsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;

public interface WeeklySummaryService {

	public void saveWeeklyData(LocalDate from, LocalDate to);
	public WeeklySummaryResponse createSummary(WeeklySummaryRequest request);
	public WeeklySummaryResponse getSummaryById(Integer weekId);
	public WeeklySummaryResponse updateSummary(WeeklySummaryRequest request);
	public List<WeeklySummaryResponse> getAllSummaries();
	public Page<WeeklySummaryResponse> generateReport(LocalDate from, LocalDate to, Pageable pageable);
	public WeeklySummary softDelete(int id);
	public void deleteSummary(Integer weekId);
	public List<WeeklySummaryResponse> getFormattedWeekRanges();
}