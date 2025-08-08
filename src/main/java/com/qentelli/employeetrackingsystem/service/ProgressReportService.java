package com.qentelli.employeetrackingsystem.service;

import org.springframework.data.domain.Page;

import com.qentelli.employeetrackingsystem.models.client.request.ProgressReportDTO;

public interface ProgressReportService {

	public void create(ProgressReportDTO dto);
	public Page<ProgressReportDTO> getAll(int page, int size);
	public void update(Long id, ProgressReportDTO dto);
	public void softDelete(Long id);
}