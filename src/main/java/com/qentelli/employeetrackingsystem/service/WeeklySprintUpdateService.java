package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;

public interface WeeklySprintUpdateService {

	public WeeklySprintUpdate createUpdate(WeeklySprintUpdateDto dto);
	public WeeklySprintUpdate updateUpdate(Integer id, WeeklySprintUpdateDto dto);
	public void deleteUpdate(Integer id);
	public boolean setWeeklySprintUpdateEnabled(Integer weeklySprintUpdateId);
	public Page<WeeklySprintUpdate> getAllUpdates(Pageable pageable);
	public List<WeeklySprintUpdate> getAllActiveUpdates();
	public List<WeeklySprintUpdate> getAllBySprintId(Long sprintId);
	public List<WeeklySprintUpdate> getActiveUpdatesByWeekId(int weekId);
	public List<WeeklySprintUpdate> getHistoricalUpdates(int currentWeekId);
	public List<WeeklySprintUpdateDto> toDtoList(List<WeeklySprintUpdate> updates);
	public WeeklySprintUpdateDto toDto(WeeklySprintUpdate update);
}