package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;

public interface WeeklySprintUpdateService {

    WeeklySprintUpdate createUpdate(WeeklySprintUpdateDto dto);
    WeeklySprintUpdate updateUpdate(Integer id, WeeklySprintUpdateDto dto);
    void deleteUpdate(Integer id); // soft delete using StatusFlag
    boolean setWeeklySprintUpdateEnabled(Integer weeklySprintUpdateId);
    Page<WeeklySprintUpdate> getAllUpdates(Pageable pageable); // returns ACTIVE only
    List<WeeklySprintUpdate> getAllActiveUpdates(); // returns ACTIVE only
    List<WeeklySprintUpdate> getAllBySprintId(Long sprintId); // returns ACTIVE only
    List<WeeklySprintUpdate> getActiveUpdatesByWeekId(int weekId); // returns ACTIVE only
    List<WeeklySprintUpdate> getHistoricalUpdates(int currentWeekId); // returns ACTIVE only
    List<WeeklySprintUpdateDto> toDtoList(List<WeeklySprintUpdate> updates);
    WeeklySprintUpdateDto toDto(WeeklySprintUpdate update);
}