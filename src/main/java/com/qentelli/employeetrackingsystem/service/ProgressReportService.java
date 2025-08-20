package com.qentelli.employeetrackingsystem.service;

import org.springframework.data.domain.Page;

import com.qentelli.employeetrackingsystem.models.client.request.ProgressReportDTO;

public interface ProgressReportService {

    void create(ProgressReportDTO dto);
    Page<ProgressReportDTO> getAll(int page, int size);
    void update(Long id, ProgressReportDTO dto);
    void softDelete(Long id);
}