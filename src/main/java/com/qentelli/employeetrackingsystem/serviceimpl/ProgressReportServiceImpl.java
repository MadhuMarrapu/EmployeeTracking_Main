package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.ProgressReport;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.exception.ReportNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ProgressReportDTO;
import com.qentelli.employeetrackingsystem.repository.ProgressReportRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.service.ProgressReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgressReportServiceImpl implements ProgressReportService {

	private final ProgressReportRepository reportRepository;
	private final ProjectRepository projectRepository;
	private final ModelMapper modelMapper;

	private double calculateCompletionPercentage(int assignedSP, int completedSP) {
		if (assignedSP <= 0)
			return 0.0;
		return Math.round((completedSP * 100.0 / assignedSP) * 10.0) / 10.0;
	}

	private Project resolveProject(Integer projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
	}

	@Override
	public void create(ProgressReportDTO dto) {
		ProgressReport entity = modelMapper.map(dto, ProgressReport.class);
		entity.setProjects(List.of(resolveProject(dto.getProjectId())));
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
			if (report.getProjects() != null && !report.getProjects().isEmpty()) {
				dto.setProjectId(report.getProjects().get(0).getProjectId());
			}
			return dto;
		});
	}

	@Override
	public void update(Long id, ProgressReportDTO dto) {
		ProgressReport existingReport = reportRepository.findById(id)
				.orElseThrow(() -> new ReportNotFoundException("No progress report found for ID: " + id));

		existingReport.setTeamLead(dto.getTeamLead());
		existingReport.setAssignedSP(dto.getAssignedSP());
		existingReport.setCompletedSP(dto.getCompletedSP());
		existingReport.setRag(dto.getRag());
		existingReport.setProjects(List.of(resolveProject(dto.getProjectId())));
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