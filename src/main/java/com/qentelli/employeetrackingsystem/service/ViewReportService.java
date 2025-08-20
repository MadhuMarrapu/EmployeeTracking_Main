package com.qentelli.employeetrackingsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.ViewReports;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.models.client.request.ViewReportRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ViewReportResponse;

public interface ViewReportService {

    ViewReportResponse saveReport(ViewReportRequest request);
    ViewReportResponse updateReport(ViewReportRequest request);
    ViewReportResponse getReportById(Integer id);

    // ✅ Returns only ACTIVE reports by default
    Page<ViewReportResponse> getAllReportsPaginated(Pageable pageable);

    // ✅ Soft delete using Status.INACTIVE
    ViewReports softDeleteSummery(Integer viewReportId);

    // ✅ Optional hard delete
    void deleteReport(Integer id);

    // ✅ Returns ACTIVE reports for given week range
    PaginatedResponse<ViewReportResponse> getTasksByWeek(LocalDate fromDate, LocalDate toDate, int page, int size);

    // ✅ Returns only ACTIVE reports by default
    List<ViewReportResponse> getAllReports();

    // ✅ Returns only ACTIVE reports by default
   // Page<ViewReports> searchViewReports(Integer personId, Integer projectId, int page, int size);

    // ✅ Optional: lifecycle-aware search
    Page<ViewReports> searchViewReports(Integer personId, Integer projectId, Status statusFlag, int page, int size);
}