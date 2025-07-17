package com.qentelli.employeetrackingsystem.serviceImpl;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklySprintUpdateService {

	private final WeeklySprintUpdateRepository weeklySprintUpdateRepository;

	private final ProjectRepository projectRepository;

	private final WeekRangeRepository weekRangeRepository;

	private final ModelMapper modelMapper;

	public WeeklySprintUpdate createUpdate(WeeklySprintUpdateDto dto) {
		WeeklySprintUpdate update = modelMapper.map(dto, WeeklySprintUpdate.class);

		Project project = projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
		WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId())
				.orElseThrow(() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));

		update.setProject(project);
		update.setWeek(week);
		return weeklySprintUpdateRepository.save(update);
	}

	public WeeklySprintUpdate updateUpdate(Integer id, WeeklySprintUpdateDto dto) {
	    WeeklySprintUpdate existing = weeklySprintUpdateRepository.findById(id)
	        .orElseThrow(() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));

	    Project project = projectRepository.findById(dto.getProjectId())
	        .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));

	    WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId())
	        .orElseThrow(() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));

	    // ✨ Map scalar fields manually
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

	    // 🛡️ Explicitly update associations and identifier
	    existing.setWeekSprintId(id); // optional if your ID strategy allows setting explicitly
	    existing.setProject(project);
	    existing.setWeek(week);

	    return weeklySprintUpdateRepository.save(existing);
	}

	public void deleteUpdate(Integer id) {
		WeeklySprintUpdate existingWeeklySprintUpdate = weeklySprintUpdateRepository.findById(id).orElseThrow(
				() -> new WeeklySprintUpdateNotFoundException("WeeklySprintUpdate not found with ID: " + id));
		existingWeeklySprintUpdate.setWeeklySprintUpdateStatus(false);
		weeklySprintUpdateRepository.save(existingWeeklySprintUpdate);
	}

	public Page<WeeklySprintUpdate> getAllUpdates(Pageable pageable) {
		return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatusTrue(pageable);
	}
}