package com.qentelli.employeetrackingsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.ViewReports;
import com.qentelli.employeetrackingsystem.models.client.request.ViewReportRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ViewReportResponse;

public interface ViewReportService {

	public ViewReportResponse saveReport(ViewReportRequest request);
	public ViewReportResponse updateReport(ViewReportRequest request);
	public ViewReportResponse getReportById(Integer id);
	public Page<ViewReportResponse> getAllReportsPaginated(Pageable pageable);
	public ViewReports softDeleteSummery(Integer viewReportId);
	public void deleteReport(Integer id);
	public PaginatedResponse<ViewReportResponse> getTasksByWeek(LocalDate fromDate, LocalDate toDate, int page, int size);
	public List<ViewReportResponse> getAllReports();
	public Page<ViewReports> searchViewReports(Integer personId, Integer projectId, int page, int size);
}
