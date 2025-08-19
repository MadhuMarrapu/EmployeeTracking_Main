package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
import com.qentelli.employeetrackingsystem.exception.ProjectNotFoundException;
import com.qentelli.employeetrackingsystem.exception.WeekRangeNotFoundException;
import com.qentelli.employeetrackingsystem.exception.WeeklySprintUpdateNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import com.qentelli.employeetrackingsystem.repository.WeeklySprintUpdateRepository;
import com.qentelli.employeetrackingsystem.service.WeeklySprintUpdateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklySprintUpdateServiceImpl implements WeeklySprintUpdateService {

	private final WeeklySprintUpdateRepository weeklySprintUpdateRepository;
	private final ProjectRepository projectRepository;
	private final WeekRangeRepository weekRangeRepository;

	@Override
	@Transactional
	public WeeklySprintUpdateDto createUpdate(WeeklySprintUpdateDto dto) {
		WeeklySprintUpdate update = new WeeklySprintUpdate();
		Project project = projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
		WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId()).orElseThrow(
				() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));
		update.setProject(project);
		update.setWeek(week);
		update.setAssignedPoints(dto.getAssignedPoints());
		update.setAssignedStoriesCount(dto.getAssignedStoriesCount());
		update.setInDevPoints(dto.getInDevPoints());
		update.setInDevStoriesCount(dto.getInDevStoriesCount());
		update.setInQaPoints(dto.getInQaPoints());
		update.setInQaStoriesCount(dto.getInQaStoriesCount());
		update.setCompletePoints(dto.getCompletePoints());
		update.setCompleteStoriesCount(dto.getCompleteStoriesCount());
		update.setBlockedPoints(dto.getBlockedPoints());
		update.setBlockedStoriesCount(dto.getBlockedStoriesCount());
		update.setCompletePercentage(dto.getCompletePercentage());
		update.setEstimationHealth(dto.getEstimationHealth());
		update.setGroomingHealth(dto.getGroomingHealth());
		update.setDifficultCount1(dto.getDifficultCount1());
		update.setDifficultCount2(dto.getDifficultCount2());
		update.setEstimationHealthStatus(dto.getEstimationHealthStatus());
		update.setGroomingHealthStatus(dto.getGroomingHealthStatus());
		update.setRiskPoints(dto.getRiskPoints());
		update.setRiskStoryCounts(dto.getRiskStoryCounts());
		update.setComments(dto.getComments());
		update.setInjectionPercentage(dto.getInjectionPercentage());
		update.setWeeklySprintUpdateStatus(StatusFlag.ACTIVE);
		update.setEnableStatus(EnableStatus.DISABLED);
		WeeklySprintUpdate saved = weeklySprintUpdateRepository.save(update);
		return convertToDto(saved);
	}

	@Override
	@Transactional
	public WeeklySprintUpdateDto updateUpdate(Integer id, WeeklySprintUpdateDto dto) {
		WeeklySprintUpdate existing = weeklySprintUpdateRepository.findById(id)
				.orElseThrow(() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));
		Project project = projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
		WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId())
				.orElseThrow(() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));
		existing.setAssignedPoints(dto.getAssignedPoints());
		existing.setAssignedStoriesCount(dto.getAssignedStoriesCount());
		existing.setInDevPoints(dto.getInDevPoints());
		existing.setInDevStoriesCount(dto.getInDevStoriesCount());
		existing.setInQaPoints(dto.getInQaPoints());
		existing.setInQaStoriesCount(dto.getInQaStoriesCount());
		existing.setCompletePoints(dto.getCompletePoints());
		existing.setCompleteStoriesCount(dto.getCompleteStoriesCount());
		existing.setBlockedPoints(dto.getBlockedPoints());
		existing.setBlockedStoriesCount(dto.getBlockedStoriesCount());
		existing.setCompletePercentage(dto.getCompletePercentage());
		existing.setEstimationHealth(dto.getEstimationHealth());
		existing.setGroomingHealth(dto.getGroomingHealth());
		existing.setDifficultCount1(dto.getDifficultCount1());
		existing.setDifficultCount2(dto.getDifficultCount2());
		existing.setEstimationHealthStatus(dto.getEstimationHealthStatus());
		existing.setGroomingHealthStatus(dto.getGroomingHealthStatus());
		existing.setRiskPoints(dto.getRiskPoints());
		existing.setRiskStoryCounts(dto.getRiskStoryCounts());
		existing.setComments(dto.getComments());
		existing.setInjectionPercentage(dto.getInjectionPercentage());
		existing.setProject(project);
		existing.setWeek(week);
		WeeklySprintUpdate saved = weeklySprintUpdateRepository.save(existing);
		return convertToDto(saved);
	}

	@Override
	@Transactional
	public void deleteUpdate(Integer id) {
		WeeklySprintUpdate update = weeklySprintUpdateRepository.findById(id)
				.orElseThrow(() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));
		update.setWeeklySprintUpdateStatus(StatusFlag.INACTIVE);
		weeklySprintUpdateRepository.save(update);
	}

	@Override
	@Transactional
	public boolean setWeeklySprintUpdateEnabled(Integer weeklySprintUpdateId) {
		WeeklySprintUpdate update = weeklySprintUpdateRepository.findById(weeklySprintUpdateId)
				.orElseThrow(() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with id: " + weeklySprintUpdateId));
		update.setEnableStatus(EnableStatus.ENABLED);
		weeklySprintUpdateRepository.save(update);
		return true;
	}

	@Override
	public Page<WeeklySprintUpdateDto> getAllUpdates(Pageable pageable) {
		Page<WeeklySprintUpdate> updates = weeklySprintUpdateRepository.findByWeeklySprintUpdateStatus(StatusFlag.ACTIVE, pageable);
		List<WeeklySprintUpdateDto> dtoList = updates.stream().map(this::convertToDto).collect(Collectors.toList());
		return new PageImpl<>(dtoList, pageable, updates.getTotalElements());
	}

	@Override
	public List<WeeklySprintUpdateDto> getAllActiveUpdates() {
		return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatus(StatusFlag.ACTIVE)
				.stream().map(this::convertToDto).toList();
	}

	@Override
	public List<WeeklySprintUpdateDto> getAllBySprintId(Long sprintId) {
		return weeklySprintUpdateRepository.findBySprintIdAndStatusFlag(sprintId, StatusFlag.ACTIVE)
				.stream().map(this::convertToDto).toList();
	}

	@Override
	public List<WeeklySprintUpdateDto> getActiveUpdatesByWeekId(int weekId) {
		return weeklySprintUpdateRepository.findByWeekIdAndStatusFlag(weekId, StatusFlag.ACTIVE)
				.stream().map(this::convertToDto).toList();
	}

	@Override
	public List<WeeklySprintUpdateDto> getHistoricalUpdates(int currentWeekId) {
		Optional<WeekRange> currentWeekOpt = weekRangeRepository.findActiveById(currentWeekId);
		if (currentWeekOpt.isEmpty()) return Collections.emptyList();

		WeekRange currentWeek = currentWeekOpt.get();
		Sprint sprint = currentWeek.getSprint();
		if (sprint == null) return Collections.emptyList();

		List<Integer> validWeekIds = weekRangeRepository.findValidHistoricalWeekIds(sprint.getSprintId(), currentWeekId);
		if (validWeekIds.isEmpty()) return Collections.emptyList();

		return weeklySprintUpdateRepository.findByWeekIdsAndStatusFlag(validWeekIds, StatusFlag.ACTIVE)
				.stream().map(this::convertToDto).toList();
	}

	// -----------------------
	// Entity to DTO converter
	// -----------------------
	private WeeklySprintUpdateDto convertToDto(WeeklySprintUpdate update) {
		WeeklySprintUpdateDto dto = new WeeklySprintUpdateDto();
		dto.setWeekSprintId(update.getWeekSprintId());
		dto.setProjectId(update.getProject().getProjectId());
		dto.setWeeekRangeId(update.getWeek().getWeekId());
		dto.setAssignedPoints(update.getAssignedPoints());
		dto.setAssignedStoriesCount(update.getAssignedStoriesCount());
		dto.setInDevPoints(update.getInDevPoints());
		dto.setInDevStoriesCount(update.getInDevStoriesCount());
		dto.setInQaPoints(update.getInQaPoints());
		dto.setInQaStoriesCount(update.getInQaStoriesCount());
		dto.setCompletePoints(update.getCompletePoints());
		dto.setCompleteStoriesCount(update.getCompleteStoriesCount());
		dto.setBlockedPoints(update.getBlockedPoints());
		dto.setBlockedStoriesCount(update.getBlockedStoriesCount());
		dto.setCompletePercentage(update.getCompletePercentage());
		dto.setEstimationHealth(update.getEstimationHealth());
		dto.setGroomingHealth(update.getGroomingHealth());
		dto.setDifficultCount1(update.getDifficultCount1());
		dto.setDifficultCount2(update.getDifficultCount2());
		dto.setEstimationHealthStatus(update.getEstimationHealthStatus());
		dto.setGroomingHealthStatus(update.getGroomingHealthStatus());
		dto.setRiskPoints(update.getRiskPoints());
		dto.setRiskStoryCounts(update.getRiskStoryCounts());
		dto.setComments(update.getComments());
		dto.setInjectionPercentage(update.getInjectionPercentage());
		return dto;
	}
}
