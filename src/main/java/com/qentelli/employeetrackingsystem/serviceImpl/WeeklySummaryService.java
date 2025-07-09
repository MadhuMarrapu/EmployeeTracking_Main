package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.WeeklySummaryRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeeklySummaryResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.WeeklySummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklySummaryService {

	@Autowired
	private WeeklySummaryRepository weeklySummaryRepository;

	@Autowired
	private ProjectRepository projectRepository;

	public WeeklySummaryResponse createSummary(WeeklySummaryRequest request) {
	    List<Project> projects = projectRepository.findAllById(request.getProjectIds());
	    if (projects.isEmpty()) {
	        throw new ResourceNotFoundException("No projects found for given IDs");
	    }

	    // ✅ Format the week range label
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
	    String weekRange = "WEEK: " + request.getWeekStartDate().format(formatter)
	                     + " To " + request.getWeekEndDate().format(formatter);

	    // ✅ Create and populate entity
	    WeeklySummary summary = new WeeklySummary();
	    summary.setWeekStartDate(request.getWeekStartDate());
	    summary.setWeekEndDate(request.getWeekEndDate());
	    summary.setUpcomingTasks(request.getUpcomingTasks());
	    summary.setListProject(projects);
	    summary.setWeekRange(weekRange); // 👈 Set the calculated value
	    summary.setCreatedAt(LocalDateTime.now());

	    // ✅ Save and build response
	    WeeklySummary savedSummary = weeklySummaryRepository.save(summary);

	    WeeklySummaryResponse response = new WeeklySummaryResponse();
	    response.setWeekId(savedSummary.getWeekId());
	    response.setWeekStartDate(savedSummary.getWeekStartDate());
	    response.setWeekEndDate(savedSummary.getWeekEndDate());
	    response.setUpcomingTasks(savedSummary.getUpcomingTasks());
	    response.setProjectNames(savedSummary.getListProject().stream()
	                             .map(Project::getProjectName)
	                             .toList());
	    response.setWeekRange(savedSummary.getWeekRange()); // 👈 Return the same range

	    return response;
	}


	public WeeklySummaryResponse getSummaryById(Integer weekId) {
		WeeklySummary summary = weeklySummaryRepository.findById(weekId)
				.orElseThrow(() -> new ResourceNotFoundException("Weekly summary not found with id: " + weekId));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
		String weekRange = "WEEK: " + summary.getWeekStartDate().format(formatter) + " To "
				+ summary.getWeekEndDate().format(formatter);

		WeeklySummaryResponse response = new WeeklySummaryResponse();
		response.setWeekId(summary.getWeekId());
		response.setWeekStartDate(summary.getWeekStartDate());
		response.setWeekEndDate(summary.getWeekEndDate());
		response.setUpcomingTasks(summary.getUpcomingTasks());
		response.setWeekRange(weekRange);
		response.setProjectNames(summary.getListProject().stream().map(Project::getProjectName).toList());
		return response;
	} 

	public List<WeeklySummaryResponse> getAllSummaries() {
		List<WeeklySummary> summaries = weeklySummaryRepository.findAll();
		System.out.println("Raw summaries count: " + summaries.size());
		summaries.forEach(summary -> System.out.println("Week ID: " + summary.getWeekId()));
		
		return summaries.stream().map(summary -> {
			WeeklySummaryResponse response = new WeeklySummaryResponse();
			response.setWeekId(summary.getWeekId());
			response.setWeekStartDate(summary.getWeekStartDate());
			response.setWeekEndDate(summary.getWeekEndDate());
			response.setUpcomingTasks(summary.getUpcomingTasks());
			response.setProjectNames(summary.getListProject().stream().map(Project::getProjectName).toList());
			response.setWeekRange("WEEK: " + summary.getWeekStartDate() + " To " + summary.getWeekEndDate());
			return response;
		}).toList();
	}

	public WeeklySummaryResponse updateSummary(WeeklySummaryRequest request) {
		Integer weekId = request.getWeekId();
		WeeklySummary summary = weeklySummaryRepository.findById(weekId)
				.orElseThrow(() -> new ResourceNotFoundException("Weekly summary not found with id: " + weekId));

		List<Project> projects = projectRepository.findAllById(request.getProjectIds());
		if (projects.isEmpty()) {
			throw new ResourceNotFoundException("No projects found for given IDs");
		}

		summary.setWeekStartDate(request.getWeekStartDate());
		summary.setWeekEndDate(request.getWeekEndDate());
		summary.setUpcomingTasks(request.getUpcomingTasks());
		summary.setListProject(projects);

		WeeklySummary updatedSummary = weeklySummaryRepository.save(summary);

		WeeklySummaryResponse response = new WeeklySummaryResponse();
		response.setWeekId(updatedSummary.getWeekId());
		response.setWeekStartDate(updatedSummary.getWeekStartDate());
		response.setWeekEndDate(updatedSummary.getWeekEndDate());
		response.setUpcomingTasks(updatedSummary.getUpcomingTasks());
		response.setProjectNames(
				updatedSummary.getListProject().stream().map(Project::getProjectName).collect(Collectors.toList()));
		return response;
	}

	// SOFT DELETE
	public WeeklySummary softDeleteSummery(Integer weekId) {
		WeeklySummary weeklySummary = weeklySummaryRepository.findById(weekId)
				.orElseThrow(() -> new ResourceNotFoundException("Weekly summary not found"));
		weeklySummary.setSoftDelete(true);
		return weeklySummaryRepository.save(weeklySummary);
	}

	// HARD DELETE
	public void deleteSummary(Integer weekId) {
		WeeklySummary summary = weeklySummaryRepository.findById(weekId)
				.orElseThrow(() -> new ResourceNotFoundException("Weekly summary not found with id: " + weekId));
		weeklySummaryRepository.delete(summary);
	}

	public Page<WeeklySummaryResponse> getAllSummariesPaginated(Pageable pageable) {
	    Page<WeeklySummary> summaries = weeklySummaryRepository.findAllBySoftDeleteFalse(pageable);

	    return summaries.map(summary -> {
	        WeeklySummaryResponse response = new WeeklySummaryResponse();
	        response.setWeekId(summary.getWeekId());
	        response.setWeekStartDate(summary.getWeekStartDate());
	        response.setWeekEndDate(summary.getWeekEndDate());
	        response.setUpcomingTasks(summary.getUpcomingTasks());
	        response.setProjectNames(summary.getListProject().stream()
	            .map(Project::getProjectName).toList());
	        response.setWeekRange("WEEK: " + summary.getWeekStartDate() + " To " + summary.getWeekEndDate());
	        return response;
	    });
	}
	
	// Get by start and end date	
	public WeeklySummaryResponse getByStartAndEndDate(LocalDate start, LocalDate end) {
	    WeeklySummary summary = weeklySummaryRepository
	        .findByWeekStartDateAndWeekEndDate(start, end)
	        .orElseThrow(() -> new ResourceNotFoundException("Week range not found"));

	    WeeklySummaryResponse response = new WeeklySummaryResponse();
	    response.setWeekId(summary.getWeekId());
	    response.setWeekStartDate(summary.getWeekStartDate());
	    response.setWeekEndDate(summary.getWeekEndDate());
	    response.setUpcomingTasks(summary.getUpcomingTasks());
	    response.setProjectNames(
	        summary.getListProject().stream()
	            .map(Project::getProjectName)
	            .toList()
	    );
	    response.setWeekRange(summary.getWeekRange());
	    return response;
	}

	}
