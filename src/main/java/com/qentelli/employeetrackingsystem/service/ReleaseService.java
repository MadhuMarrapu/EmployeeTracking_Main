package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Release;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;

public interface ReleaseService {

    Release createRelease(ReleaseRequestDTO dto);
    List<ReleaseResponseDTO> getAllReleases();
    Page<ReleaseResponseDTO> getPaginatedReleases(Pageable pageable);
    List<ReleaseResponseDTO> getReleasesByWeekId(int weekId);
    List<ReleaseResponseDTO> getReleasesBySprintId(int sprintId);
    Release updateRelease(Long id, ReleaseRequestDTO dto);
    void deleteRelease(Long id); // soft delete
    List<ReleaseResponseDTO> getReleasesByStatusFlag(Status statusFlag);
}