package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
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
	private final ModelMapper modelMapper;

	@Override
	public WeeklySprintUpdate createUpdate(WeeklySprintUpdateDto dto) {
		WeeklySprintUpdate update = modelMapper.map(dto, WeeklySprintUpdate.class);

		Project project = projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
		WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId()).orElseThrow(
				() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));

		update.setProject(project);
		update.setWeek(week);
		update.setEstimationHealthStatus(dto.getEstimationHealthStatus());
		update.setGroomingHealthStatus(dto.getGroomingHealthStatus());
		update.setRiskPoints(dto.getRiskPoints());
		update.setRiskStoryCounts(dto.getRiskStoryCounts());
		update.setComments(dto.getComments());
		update.setInjectionPercentage(dto.getInjectionPercentage());

		return weeklySprintUpdateRepository.save(update);
	}

	@Override
	public WeeklySprintUpdate updateUpdate(Integer id, WeeklySprintUpdateDto dto) {
		WeeklySprintUpdate existing = weeklySprintUpdateRepository.findById(id).orElseThrow(
				() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));

		Project project = projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
		WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId()).orElseThrow(
				() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));

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
		existing.setWeeklySprintUpdateStatus(dto.isWeeklySprintUpdateStatus());
		existing.setEstimationHealthStatus(dto.getEstimationHealthStatus());
		existing.setGroomingHealthStatus(dto.getGroomingHealthStatus());
		existing.setRiskPoints(dto.getRiskPoints());
		existing.setRiskStoryCounts(dto.getRiskStoryCounts());
		existing.setComments(dto.getComments());
		existing.setInjectionPercentage(dto.getInjectionPercentage());
		existing.setProject(project);
		existing.setWeek(week);

		return weeklySprintUpdateRepository.save(existing);
	}

	@Override
	public void deleteUpdate(Integer id) {
		WeeklySprintUpdate update = weeklySprintUpdateRepository.findById(id).orElseThrow(
				() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));
		update.setWeeklySprintUpdateStatus(false);
		weeklySprintUpdateRepository.save(update);
	}

	@Override
	public boolean setWeeklySprintUpdateEnabled(Integer weeklySprintUpdateId) {
		WeeklySprintUpdate update = weeklySprintUpdateRepository.findById(weeklySprintUpdateId)
				.orElseThrow(() -> new WeeklySprintUpdateNotFoundException(
						"WeeklySprintUpdate not found with id: " + weeklySprintUpdateId));
		update.setEnabled(true);
		weeklySprintUpdateRepository.save(update);
		return true;
	}

	@Override
	public Page<WeeklySprintUpdate> getAllUpdates(Pageable pageable) {
		return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatusTrue(pageable);
	}

	@Override
	public List<WeeklySprintUpdate> getAllActiveUpdates() {
		return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatusTrue();
	}

	@Override
	public List<WeeklySprintUpdate> getAllBySprintId(Long sprintId) {
		return weeklySprintUpdateRepository.findActiveBySprintId(sprintId);
	}

	@Override
	public List<WeeklySprintUpdate> getActiveUpdatesByWeekId(int weekId) {
		return weeklySprintUpdateRepository.findActiveByWeekId(weekId);
	}

	@Override
	public List<WeeklySprintUpdate> getHistoricalUpdates(int currentWeekId) {
	    // Validate currentWeekId and ensure it's not soft-deleted
	    if (currentWeekId <= 1 || !weekRangeRepository.existsByIdAndSoftDeleteFalse(currentWeekId)) {
	        return Collections.emptyList();
	    }
	    // Generate all previous week IDs using IntStream
	    List<Integer> previousWeekIds = IntStream.rangeClosed(1, currentWeekId - 1).boxed().toList();
	    // Filter out soft-deleted week IDs
	    Set<Integer> activeWeekIds = weekRangeRepository.findAll().stream()
	        .filter(wr -> !wr.isSoftDelete())
	        .map(WeekRange::getWeekId)
	        .collect(Collectors.toSet());
	    List<Integer> validPreviousWeekIds = previousWeekIds.stream()
	        .filter(activeWeekIds::contains)
	        .toList();
	    if (validPreviousWeekIds.isEmpty()) {
	        return Collections.emptyList();
	    }  
	    return  weeklySprintUpdateRepository.findActiveByWeekIds(validPreviousWeekIds);
	}

	public WeeklySprintUpdateDto toDto(WeeklySprintUpdate update) {
		WeeklySprintUpdateDto dto = modelMapper.map(update, WeeklySprintUpdateDto.class);

		if (update.getProject() != null) {
			dto.setProjectId(update.getProject().getProjectId());
			dto.setProjectName(update.getProject().getProjectName());
		}

		if (update.getWeek() != null) {
			dto.setWeeekRangeId(update.getWeek().getWeekId());
			if (update.getWeek().getSprint() != null) {
				dto.setSprintNumber(update.getWeek().getSprint().getSprintNumber());
			}
		}

		return dto;
	}

	public List<WeeklySprintUpdateDto> toDtoList(List<WeeklySprintUpdate> updates) {
		return updates.stream().map(this::toDto).toList();
	}
}