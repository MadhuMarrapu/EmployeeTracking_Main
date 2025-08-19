package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.ProgressReport;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
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

    private double calculateCompletionPercentage(int assignedSP, int completedSP) {
        if (assignedSP <= 0) return 0.0;
        return Math.round((completedSP * 100.0 / assignedSP) * 10.0) / 10.0;
    }

    private Project resolveProject(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
    }

    @Override
    public void create(ProgressReportDTO dto) {
        ProgressReport entity = toEntity(dto);
        entity.setStatusFlag(StatusFlag.ACTIVE); // ✅ default to ACTIVE
        reportRepository.save(entity);
    }

    @Override
    public Page<ProgressReportDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("snapshotDate").descending());
        return reportRepository.findByStatusFlag(StatusFlag.ACTIVE, pageable)
                .map(this::toDTO); // ✅ filter by ACTIVE
    }

    @Override
    public void update(Long id, ProgressReportDTO dto) {
        ProgressReport existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("No progress report found for ID: " + id));

        existingReport.setTeamLead(dto.getTeamLead());
        existingReport.setAssignedSP(dto.getAssignedSP());
        existingReport.setCompletedSP(dto.getCompletedSP());
        existingReport.setRag(dto.getRag());
       
        existingReport.setSnapshotDate(LocalDateTime.now());
        existingReport.setCompletionPercentage(
                calculateCompletionPercentage(dto.getAssignedSP(), dto.getCompletedSP()));
        existingReport.setProject(resolveProject(dto.getProjectId()));

        reportRepository.save(existingReport);
    }

    @Override
    public void softDelete(Long id) {
        ProgressReport existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ReportNotFoundException("Cannot delete. No report with ID: " + id));
        existingReport.setStatusFlag(StatusFlag.INACTIVE); // ✅ soft delete
        reportRepository.save(existingReport);
    }

    private ProgressReport toEntity(ProgressReportDTO dto) {
        ProgressReport entity = new ProgressReport();
        entity.setId(dto.getReportId());
        entity.setTeamLead(dto.getTeamLead());
        entity.setAssignedSP(dto.getAssignedSP());
        entity.setCompletedSP(dto.getCompletedSP());
        entity.setRag(dto.getRag());
        entity.setStatusFlag(StatusFlag.ACTIVE); // ✅ lifecycle
        entity.setSnapshotDate(LocalDateTime.now());
        entity.setCompletionPercentage(
                calculateCompletionPercentage(dto.getAssignedSP(), dto.getCompletedSP()));
        entity.setProject(resolveProject(dto.getProjectId()));
        return entity;
    }

    private ProgressReportDTO toDTO(ProgressReport report) {
        ProgressReportDTO dto = new ProgressReportDTO();
        dto.setReportId(report.getId());
        dto.setTeamLead(report.getTeamLead());
        dto.setAssignedSP(report.getAssignedSP());
        dto.setCompletedSP(report.getCompletedSP());
        dto.setRag(report.getRag());
        dto.setSnapshotDate(report.getSnapshotDate());
        dto.setCompletionPercentage(
                calculateCompletionPercentage(report.getAssignedSP(), report.getCompletedSP()));
        if (report.getProject() != null) {
            dto.setProjectId(report.getProject().getProjectId());
            dto.setProjectName(report.getProject().getProjectName());
        }
        return dto;
    }
}