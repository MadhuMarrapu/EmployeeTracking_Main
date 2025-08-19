package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public WeeklySprintUpdate createUpdate(WeeklySprintUpdateDto dto) {
        WeeklySprintUpdate update = modelMapper.map(dto, WeeklySprintUpdate.class);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + dto.getProjectId()));
        WeekRange week = weekRangeRepository.findById(dto.getWeeekRangeId())
                .orElseThrow(() -> new WeekRangeNotFoundException("WeekRange not found with ID: " + dto.getWeeekRangeId()));

        update.setProject(project);
        update.setWeek(week);
        update.setEstimationHealthStatus(dto.getEstimationHealthStatus());
        update.setGroomingHealthStatus(dto.getGroomingHealthStatus());
        update.setRiskPoints(dto.getRiskPoints());
        update.setRiskStoryCounts(dto.getRiskStoryCounts());
        update.setComments(dto.getComments());
        update.setInjectionPercentage(dto.getInjectionPercentage());
        update.setWeeklySprintUpdateStatus(StatusFlag.ACTIVE);
        update.setEnableStatus(EnableStatus.DISABLED);

        return weeklySprintUpdateRepository.save(update);
    }

    @Override
    @Transactional
    public WeeklySprintUpdate updateUpdate(Integer id, WeeklySprintUpdateDto dto) {
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

        return weeklySprintUpdateRepository.save(existing);
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
    public Page<WeeklySprintUpdate> getAllUpdates(Pageable pageable) {
        return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatus(StatusFlag.ACTIVE, pageable);
    }

    @Override
    public List<WeeklySprintUpdate> getAllActiveUpdates() {
        return weeklySprintUpdateRepository.findByWeeklySprintUpdateStatus(StatusFlag.ACTIVE);
    }

    @Override
    public List<WeeklySprintUpdate> getAllBySprintId(Long sprintId) {
        return weeklySprintUpdateRepository.findBySprintIdAndStatusFlag(sprintId, StatusFlag.ACTIVE);
    }

    @Override
    public List<WeeklySprintUpdate> getActiveUpdatesByWeekId(int weekId) {
        return weeklySprintUpdateRepository.findByWeekIdAndStatusFlag(weekId, StatusFlag.ACTIVE);
    }

    @Override
    public List<WeeklySprintUpdate> getHistoricalUpdates(int currentWeekId) {
        Optional<WeekRange> currentWeekOpt = weekRangeRepository.findActiveById(currentWeekId);
        if (currentWeekOpt.isEmpty()) return Collections.emptyList();

        WeekRange currentWeek = currentWeekOpt.get();
        Sprint sprint = currentWeek.getSprint();
        if (sprint == null) return Collections.emptyList();

        List<Integer> validWeekIds = weekRangeRepository.findValidHistoricalWeekIds(sprint.getSprintId(), currentWeekId);
        if (validWeekIds.isEmpty()) return Collections.emptyList();

        return weeklySprintUpdateRepository.findByWeekIdsAndStatusFlag(validWeekIds, StatusFlag.ACTIVE);
    }

    @Override
    public WeeklySprintUpdateDto toDto(WeeklySprintUpdate update) {
        WeeklySprintUpdateDto dto = modelMapper.map(update, WeeklySprintUpdateDto.class);
        Optional.ofNullable(update.getProject()).ifPresent(project -> {
            dto.setProjectId(project.getProjectId());
            dto.setProjectName(project.getProjectName());
        });
        Optional.ofNullable(update.getWeek()).ifPresent(week -> {
            dto.setWeeekRangeId(week.getWeekId());
            Optional.ofNullable(week.getSprint()).ifPresent(sprint -> {
                dto.setSprintNumber(sprint.getSprintNumber());
            });
        });
        return dto;
    }

    @Override
    public List<WeeklySprintUpdateDto> toDtoList(List<WeeklySprintUpdate> updates) {
        return updates == null || updates.isEmpty()
                ? Collections.emptyList()
                : updates.stream().map(this::toDto).toList();
    }
}