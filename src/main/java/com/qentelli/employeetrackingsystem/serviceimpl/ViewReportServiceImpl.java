package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Task;
import com.qentelli.employeetrackingsystem.entity.ViewReports;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ViewReportRequest;
import com.qentelli.employeetrackingsystem.models.client.response.PaginatedResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ViewReportResponse;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.ViewreportRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import com.qentelli.employeetrackingsystem.service.ViewReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewReportServiceImpl implements ViewReportService {

	private static final String REPORT_NOT_FOUND = "Report not found";
	private static final String WEEKLY_SUMMARY_NOT_FOUND = "Weekly summary not found";
	private static final String PROJECT_NOT_FOUND = "Project not found";
	private static final String PERSON_NOT_FOUND = "Person not found";

	private final ViewreportRepository viewReportRepository;
	private final WeekRangeRepository weekRangeRepository;
	private final ProjectRepository projectRepository;
	private final PersonRepository personRepository;

	@Override
	public ViewReportResponse saveReport(ViewReportRequest request) {
		WeekRange weekRange = weekRangeRepository.findById(request.getWeekId())
				.orElseThrow(() -> new RuntimeException(WEEKLY_SUMMARY_NOT_FOUND + " with id: " + request.getWeekId()));
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND + " with id: " + request.getProjectId()));
		Person person = personRepository.findByPersonId(request.getPersonId())
				.orElseThrow(() -> new RuntimeException(PERSON_NOT_FOUND + " with id: " + request.getPersonId()));
		ViewReports report = new ViewReports();
		report.setTaskName(request.getTaskName());
		report.setTaskStatus(request.getTaskStatus());
		report.setTaskStartDate(request.getTaskStartDate());
		report.setTaskEndDate(request.getTaskEndDate());
		report.setComments(request.getComments());
		report.setProject(project);
		report.setPerson(person);
		report.setWeekRange(weekRange);
		report.setTask(new Task(request.getSummary(), request.getKeyAccomplishment(), request.getComments()));
		ViewReports saved = viewReportRepository.save(report);
		ViewReportResponse response = new ViewReportResponse();
		response.setViewReportId(saved.getViewReportId());
		response.setTaskName(saved.getTaskName());
		response.setTaskStatus(saved.getTaskStatus());
		response.setSummary(saved.getTask().getSummary());
		response.setKeyAccomplishment(saved.getTask().getKeyAccomplishment());
		response.setComments(saved.getComments());
		response.setProjectName(saved.getProject().getProjectName());
		response.setPersonName(saved.getPerson().getFirstName() + " " + saved.getPerson().getLastName());
		response.setTaskStartDate(saved.getTaskStartDate());
		response.setTaskEndDate(saved.getTaskEndDate());
		response.setCreatedAt(saved.getCreatedAt());
		response.setCreatedBy(saved.getCreatedBy());
		return response;
	}

	@Override
	public ViewReportResponse updateReport(ViewReportRequest request) {
		ViewReports report = viewReportRepository.findById(request.getViewReportId())
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND));
		WeekRange weekRange = weekRangeRepository.findById(request.getWeekId())
				.orElseThrow(() -> new RuntimeException(WEEKLY_SUMMARY_NOT_FOUND + " with id: " + request.getWeekId()));
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND + " with id: " + request.getProjectId()));
		Person person = personRepository.findByPersonId(request.getPersonId())
				.orElseThrow(() -> new RuntimeException(PERSON_NOT_FOUND + " with id: " + request.getPersonId()));
		report.setTaskName(request.getTaskName());
		report.setTaskStatus(request.getTaskStatus());
		report.setTaskStartDate(request.getTaskStartDate());
		report.setTaskEndDate(request.getTaskEndDate());
		report.setProject(project);
		report.setPerson(person);
		report.setWeekRange(weekRange);
		report.setTask(new Task(request.getSummary(), request.getKeyAccomplishment(), request.getComments()));
		ViewReports updated = viewReportRepository.save(report);
		ViewReportResponse response = new ViewReportResponse();
		response.setViewReportId(updated.getViewReportId());
		response.setTaskName(updated.getTaskName());
		response.setTaskStatus(updated.getTaskStatus());
		response.setSummary(updated.getTask().getSummary());
		response.setKeyAccomplishment(updated.getTask().getKeyAccomplishment());
		response.setComments(updated.getComments());
		response.setProjectName(updated.getProject().getProjectName());
		response.setPersonName(updated.getPerson().getFirstName() + " " + updated.getPerson().getLastName());
		response.setTaskStartDate(updated.getTaskStartDate());
		response.setTaskEndDate(updated.getTaskEndDate());
		response.setCreatedAt(updated.getCreatedAt());
		response.setCreatedBy(updated.getCreatedBy());
		return response;
	}

	@Override
	public ViewReportResponse getReportById(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND + " with id: " + id));
		ViewReportResponse response = new ViewReportResponse();
		response.setViewReportId(report.getViewReportId());
		response.setWeekRange(new WeekRangeResponse(report.getWeekRange().getWeekId(),
				report.getWeekRange().getWeekFromDate(), report.getWeekRange().getWeekToDate()));
		response.setTaskName(report.getTaskName());
		response.setTaskStatus(report.getTaskStatus());
		response.setSummary(report.getTask().getSummary());
		response.setKeyAccomplishment(report.getTask().getKeyAccomplishment());
		response.setComments(report.getComments());
		response.setProjectName(report.getProject().getProjectName());
		response.setPersonName(report.getPerson().getFirstName() + " " + report.getPerson().getLastName());
		response.setTaskStartDate(report.getTaskStartDate());
		response.setTaskEndDate(report.getTaskEndDate());
		response.setCreatedAt(report.getCreatedAt());
		response.setCreatedBy(report.getCreatedBy());
		return response;
	}

	@Override
	public Page<ViewReportResponse> getAllReportsPaginated(Pageable pageable) {
		Page<ViewReports> page = viewReportRepository.findAll(pageable);
		return page.map(this::mapToResponse);
	}

	@Override
	public ViewReports softDeleteSummery(Integer viewReportId) {
		ViewReports viewReports = viewReportRepository.findById(viewReportId)
				.orElseThrow(() -> new ResourceNotFoundException("viewReportId not found"));
		viewReports.setSoftDelete(true);
		return viewReportRepository.save(viewReports);
	}

	@Override
	public void deleteReport(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND + " with id: " + id));
		viewReportRepository.delete(report);
	}

	@Override
	public PaginatedResponse<ViewReportResponse> getTasksByWeek(LocalDate fromDate, LocalDate toDate, int page,
			int size) {
		List<ViewReports> reports = viewReportRepository.findByWeekRange(fromDate, toDate);
		List<ViewReportResponse> allResponses = reports.stream().map(this::mapToResponse).collect(Collectors.toList());
		int start = Math.min(page * size, allResponses.size());
		int end = Math.min(start + size, allResponses.size());
		List<ViewReportResponse> pagedResponses = allResponses.subList(start, end);
		return new PaginatedResponse<>(pagedResponses, page, size, allResponses.size(),
				(int) Math.ceil((double) allResponses.size() / size), end == allResponses.size());
	}

	@Override
	public List<ViewReportResponse> getAllReports() {
		List<ViewReports> reports = viewReportRepository.findBySoftDeleteFalse();
		return reports.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	private ViewReportResponse mapToResponse(ViewReports report) {
		return new ViewReportResponse(report.getViewReportId(),
				report.getWeekRange() != null
						? new WeekRangeResponse(report.getWeekRange().getWeekId(),
								report.getWeekRange().getWeekFromDate(), report.getWeekRange().getWeekToDate())
						: null,
				report.getTaskName(), report.getTaskStatus(),
				report.getTask() != null ? report.getTask().getSummary() : null,
				report.getTask() != null ? report.getTask().getKeyAccomplishment() : null, report.getComments(),
				report.getTask() != null ? report.getTask().getUpcomingTasks() : null,
				report.getProject() != null ? report.getProject().getProjectName() : null,
				report.getPerson() != null ? report.getPerson().getFirstName() + " " + report.getPerson().getLastName()
						: null,
				report.getTaskStartDate(), report.getTaskEndDate(), report.getCreatedAt(), report.getCreatedBy());
	}

	@Override
	public Page<ViewReports> searchViewReports(Integer personId, Integer projectId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("viewReportId").descending());
		if (personId != null && projectId != null) {
			return viewReportRepository.findBySoftDeleteFalseAndPerson_PersonIdAndProject_ProjectId(personId, projectId,
					pageable);
		} else if (personId != null) {
			return viewReportRepository.findBySoftDeleteFalseAndPerson_PersonId(personId, pageable);
		} else if (projectId != null) {
			return viewReportRepository.findBySoftDeleteFalseAndProject_ProjectId(projectId, pageable);
		} else {
			return viewReportRepository.findBySoftDeleteFalse(pageable);
		}
	}
}
