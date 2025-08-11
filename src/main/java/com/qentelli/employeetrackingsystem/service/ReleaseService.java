package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Release;
import com.qentelli.employeetrackingsystem.models.client.request.ReleaseRequestDTO;
import com.qentelli.employeetrackingsystem.models.client.response.ReleaseResponseDTO;

public interface ReleaseService {

	public Release createRelease(ReleaseRequestDTO dto);
	public List<ReleaseResponseDTO> getAllReleases();
	public Page<ReleaseResponseDTO> getPaginatedReleases(Pageable pageable);
	public List<ReleaseResponseDTO> getReleasesByWeekId(int weekId);
	public List<ReleaseResponseDTO> getReleasesBySprintId(int sprintId);
	public Release updateRelease(Long id, ReleaseRequestDTO dto);
	public void deleteRelease(Long id);
}