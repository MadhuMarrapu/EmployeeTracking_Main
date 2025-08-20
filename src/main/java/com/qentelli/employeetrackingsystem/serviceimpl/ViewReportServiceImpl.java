package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDate;
import java.util.List;

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
import com.qentelli.employeetrackingsystem.entity.enums.Status;
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

	private static final String REPORT_NOT_FOUND = "Report not found with id: ";
	private static final String WEEKLY_SUMMARY_NOT_FOUND = "Weekly summary not found with id: ";
	private static final String PROJECT_NOT_FOUND = "Project not found with id: ";
	private static final String PERSON_NOT_FOUND = "Person not found with id: ";

	private final ViewreportRepository viewReportRepository;
	private final WeekRangeRepository weekRangeRepository;
	private final ProjectRepository projectRepository;
	private final PersonRepository personRepository;

	@Override
	public ViewReportResponse saveReport(ViewReportRequest request) {
		WeekRange weekRange = weekRangeRepository.findById(request.getWeekId())
				.orElseThrow(() -> new RuntimeException(WEEKLY_SUMMARY_NOT_FOUND + request.getWeekId()));
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND + request.getProjectId()));
		Person person = personRepository.findByPersonId(request.getPersonId())
				.orElseThrow(() -> new RuntimeException(PERSON_NOT_FOUND + request.getPersonId()));
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
		report.setStatusFlag(Status.ACTIVE); 
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
		response.setStatusFlag(saved.getStatusFlag()); 
		return response;
	}

	@Override
	public ViewReportResponse updateReport(ViewReportRequest request) {
		ViewReports report = viewReportRepository.findById(request.getViewReportId())
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND));
		WeekRange weekRange = weekRangeRepository.findById(request.getWeekId())
				.orElseThrow(() -> new RuntimeException(WEEKLY_SUMMARY_NOT_FOUND + request.getWeekId()));
		Project project = projectRepository.findById(request.getProjectId())
				.orElseThrow(() -> new RuntimeException(PROJECT_NOT_FOUND + request.getProjectId()));
		Person person = personRepository.findByPersonId(request.getPersonId())
				.orElseThrow(() -> new RuntimeException(PERSON_NOT_FOUND + request.getPersonId()));
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
		response.setStatusFlag(updated.getStatusFlag());
		return response;
	}

	@Override
	public ViewReportResponse getReportById(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND + id));
		ViewReportResponse response = new ViewReportResponse();
		response.setViewReportId(report.getViewReportId());
		if (report.getWeekRange() != null) {
			response.setWeekRange(new WeekRangeResponse(report.getWeekRange().getWeekId(),
					report.getWeekRange().getWeekFromDate(), report.getWeekRange().getWeekToDate()));
		}
		response.setTaskName(report.getTaskName());
		response.setTaskStatus(report.getTaskStatus());

		if (report.getTask() != null) {
			response.setSummary(report.getTask().getSummary());
			response.setKeyAccomplishment(report.getTask().getKeyAccomplishment());
		}

		response.setComments(report.getComments());

		if (report.getProject() != null) {
			response.setProjectName(report.getProject().getProjectName());
		}

		if (report.getPerson() != null) {
			response.setPersonName(report.getPerson().getFirstName() + " " + report.getPerson().getLastName());
		}

		response.setTaskStartDate(report.getTaskStartDate());
		response.setTaskEndDate(report.getTaskEndDate());
		response.setCreatedAt(report.getCreatedAt());
		response.setCreatedBy(report.getCreatedBy());
		response.setStatusFlag(report.getStatusFlag()); 
		return response;
	}

	@Override
	public Page<ViewReportResponse> getAllReportsPaginated(Pageable pageable) {
		Page<ViewReports> page = viewReportRepository.findByStatusFlag(Status.ACTIVE, pageable); 																									// Lifecycle-aware
		return page.map(this::mapToResponse);
	}

	@Override
	public ViewReports softDeleteSummery(Integer viewReportId) {
		ViewReports viewReports = viewReportRepository.findById(viewReportId)
				.orElseThrow(() -> new ResourceNotFoundException("viewReportId not found"));
		viewReports.setStatusFlag(Status.INACTIVE); 
		return viewReportRepository.save(viewReports);
	}

	@Override
	public void deleteReport(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(REPORT_NOT_FOUND + id));
		viewReportRepository.delete(report); 
	}

	@Override
	public PaginatedResponse<ViewReportResponse> getTasksByWeek(LocalDate fromDate, LocalDate toDate, int page,
			int size) {
		List<ViewReports> reports = viewReportRepository.findByWeekRangeAndStatusFlag(fromDate, toDate,
				Status.ACTIVE); 
		List<ViewReportResponse> allResponses = reports.stream().map(this::mapToResponse).toList();

		int start = Math.min(page * size, allResponses.size());
		int end = Math.min(start + size, allResponses.size());
		List<ViewReportResponse> pagedResponses = allResponses.subList(start, end);

		return new PaginatedResponse<>(pagedResponses, page, size, allResponses.size(),
				(int) Math.ceil((double) allResponses.size() / size), end == allResponses.size());
	}

	@Override
	public List<ViewReportResponse> getAllReports() {
		List<ViewReports> reports = viewReportRepository.findByStatusFlag(Status.ACTIVE); // ✅ Lifecycle-aware
		return reports.stream().map(this::mapToResponse).toList();
	}

	private ViewReportResponse mapToResponse(ViewReports report) {
		WeekRangeResponse weekRangeResponse = null;
		if (report.getWeekRange() != null) {
			weekRangeResponse = new WeekRangeResponse(report.getWeekRange().getWeekId(),
					report.getWeekRange().getWeekFromDate(), report.getWeekRange().getWeekToDate());
			weekRangeResponse.setStatusFlag(report.getWeekRange().getStatusFlag());
			weekRangeResponse.setEnableStatus(report.getWeekRange().getEnableStatus());
		}

		return new ViewReportResponse(report.getViewReportId(), weekRangeResponse, report.getTaskName(),
				report.getTaskStatus(), report.getTask() != null ? report.getTask().getSummary() : null,
				report.getTask() != null ? report.getTask().getKeyAccomplishment() : null, report.getComments(),
				report.getTask() != null ? report.getTask().getUpcomingTasks() : null,
				report.getProject() != null ? report.getProject().getProjectName() : null,
				report.getPerson() != null ? report.getPerson().getFirstName() + " " + report.getPerson().getLastName()
						: null,
				report.getTaskStartDate(), report.getTaskEndDate(), report.getCreatedAt(), report.getCreatedBy(),
				report.getStatusFlag() 
		);
	}

	@Override
	public Page<ViewReports> searchViewReports(Integer personId, Integer projectId, Status statusFlag, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("viewReportId").descending());

		if (personId != null && projectId != null) {
			return viewReportRepository.findByStatusFlagAndPerson_PersonIdAndProject_ProjectId(statusFlag, personId,
					projectId, pageable);
		} else if (personId != null) {
			return viewReportRepository.findByStatusFlagAndPerson_PersonId(statusFlag, personId, pageable);
		} else if (projectId != null) {
			return viewReportRepository.findByStatusFlagAndProject_ProjectId(statusFlag, projectId, pageable);
		} else {
			return viewReportRepository.findByStatusFlag(statusFlag, pageable);
		}
	}

	

}
