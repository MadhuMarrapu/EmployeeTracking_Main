package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.models.client.request.WeeklySprintUpdateDto;

public interface WeeklySprintUpdateService {

    WeeklySprintUpdateDto createUpdate(WeeklySprintUpdateDto dto);

    WeeklySprintUpdateDto updateUpdate(Integer id, WeeklySprintUpdateDto dto);

    void deleteUpdate(Integer id); // soft delete using StatusFlag

    boolean setWeeklySprintUpdateEnabled(Integer weeklySprintUpdateId);

    Page<WeeklySprintUpdateDto> getAllUpdates(Pageable pageable); // returns ACTIVE only

    List<WeeklySprintUpdateDto> getAllActiveUpdates(); // returns ACTIVE only

    List<WeeklySprintUpdateDto> getAllBySprintId(Long sprintId); // returns ACTIVE only

    List<WeeklySprintUpdateDto> getActiveUpdatesByWeekId(int weekId); // returns ACTIVE only

    List<WeeklySprintUpdateDto> getHistoricalUpdates(int currentWeekId); // returns ACTIVE only
}