package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.ProgressReport;
import com.qentelli.employeetrackingsystem.exception.ReportNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ProgressReportDTO;
import com.qentelli.employeetrackingsystem.repository.ProgressReportRepository;
import com.qentelli.employeetrackingsystem.service.ProgressReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressReportServiceImpl implements ProgressReportService {

	private final ProgressReportRepository reportRepository;
	private final ModelMapper modelMapper;

	private double calculateCompletionPercentage(int assignedSP, int completedSP) {
		if (assignedSP <= 0)
			return 0.0;
		return Math.round((completedSP * 100.0 / assignedSP) * 10.0) / 10.0;
	}

	@Override
	public void create(ProgressReportDTO dto) {
		ProgressReport entity = modelMapper.map(dto, ProgressReport.class);
		entity.setCompletionPercentage(calculateCompletionPercentage(dto.getAssignedSP(), dto.getCompletedSP()));
		entity.setSnapshotDate(LocalDateTime.now());
		reportRepository.save(entity);
	}

	@Override
	public Page<ProgressReportDTO> getAll(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("snapshotDate").descending());
		return reportRepository.findByProgressReportStatusTrue(pageable).map(report -> {
			ProgressReportDTO dto = modelMapper.map(report, ProgressReportDTO.class);
			dto.setCompletionPercentage(calculateCompletionPercentage(report.getAssignedSP(), report.getCompletedSP()));
			return dto;
		});
	}

	@Override
	public void update(Long id, ProgressReportDTO dto) {
		ProgressReport existingReport = reportRepository.findById(id)
				.orElseThrow(() -> new ReportNotFoundException("No progress report found for ID: " + id));
		existingReport.setTeam(dto.getTeam());
		existingReport.setTcbLead(dto.getTcbLead());
		existingReport.setAssignedSP(dto.getAssignedSP());
		existingReport.setCompletedSP(dto.getCompletedSP());
		existingReport.setRag(dto.getRag());
		existingReport
				.setCompletionPercentage(calculateCompletionPercentage(dto.getAssignedSP(), dto.getCompletedSP()));
		existingReport.setSnapshotDate(LocalDateTime.now());
		reportRepository.save(existingReport);
	}

	@Override
	public void softDelete(Long id) {
		ProgressReport existingReport = reportRepository.findById(id)
				.orElseThrow(() -> new ReportNotFoundException("Cannot delete. No report with ID: " + id));
		existingReport.setProgressReportStatus(false);
		reportRepository.save(existingReport);
	}
}