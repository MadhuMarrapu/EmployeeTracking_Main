package com.qentelli.employeetrackingsystem.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;

public class WeeklySummaryMapper {

	private WeeklySummaryMapper() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static WeeklySummary toEntity(WeeklySummaryRequest dto, List<Project> projects) {
		WeeklySummary summary = new WeeklySummary();
		summary.setWeekStartDate(dto.getWeekStartDate());
		summary.setWeekEndDate(dto.getWeekEndDate());
		summary.setUpcomingTasks(dto.getUpcomingTasks());
		summary.setStatus(dto.isStatus());
		summary.setCreatedAt(dto.getCreatedAt());
		summary.setCreatedBy(dto.getCreatedBy());
		summary.setUpdatedAt(dto.getUpdatedAt());
		summary.setUpdatedBy(dto.getUpdatedBy());
		summary.setListProject(projects);
		return summary;
	}

	public static WeeklySummaryResponse toDto(WeeklySummary entity) {
		WeeklySummaryResponse dto = new WeeklySummaryResponse();
		dto.setWeekId(entity.getWeekId());
		dto.setWeekStartDate(entity.getWeekStartDate());
		dto.setWeekEndDate(entity.getWeekEndDate());
		dto.setUpcomingTasks(entity.getUpcomingTasks());
		dto.setStatus(entity.isStatus());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setUpdatedBy(entity.getUpdatedBy());
		dto.setProjectNames(entity.getListProject().stream().map(Project::getProjectName).collect(Collectors.toList()));
		return dto;
	}
}
