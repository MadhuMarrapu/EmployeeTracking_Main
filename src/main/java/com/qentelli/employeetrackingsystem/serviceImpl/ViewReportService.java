package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Task;
import com.qentelli.employeetrackingsystem.entity.User;
import com.qentelli.employeetrackingsystem.entity.ViewReports;
import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.exception.ResourceNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.ViewReportRequest;
import com.qentelli.employeetrackingsystem.models.client.response.ViewReportResponse;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.repository.UserRepository;
import com.qentelli.employeetrackingsystem.repository.ViewreportRepository;
import com.qentelli.employeetrackingsystem.repository.WeeklySummaryRepository;

@Service
public class ViewReportService {
	
	@Autowired
	private ViewreportRepository viewReportRepository;
	
    @Autowired
    private WeeklySummaryRepository weeklySummaryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ViewReportResponse saveReport(ViewReportRequest request) {
        WeeklySummary summary = weeklySummaryRepository.findById(request.getWeekId())
                .orElseThrow(() -> new RuntimeException("Weekly summary not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findByEmployeeId(request.getEmployeeId())
        	    .orElseThrow(() -> new RuntimeException("User not found"));


        ViewReports report = new ViewReports();
        report.setTaskName(request.getTaskName());
        report.setTaskStatus(request.getTaskStatus());
        report.setTaskStartDate(request.getTaskStartDate());
        report.setTaskEndDate(request.getTaskEndDate());
        report.setComments(request.getComments());
        report.setProject(project);
        report.setUser(user);
        report.setWeeklySummary(summary);
        report.setTask(new Task(request.getSummary(), request.getKeyAccomplishment()));

        ViewReports saved = viewReportRepository.save(report);

        ViewReportResponse response = new ViewReportResponse();
        response.setViewReportId(saved.getViewReportId());
        response.setTaskName(saved.getTaskName());
        response.setTaskStatus(saved.getTaskStatus());
        response.setSummary(saved.getTask().getSummary());
        response.setKeyAccomplishment(saved.getTask().getKeyAccomplishment());
        response.setComments(saved.getComments());
        response.setProjectName(saved.getProject().getProjectName());
        response.setEmployeeName(saved.getUser().getFirstName() + " " + saved.getUser().getLastName());
        response.setTaskStartDate(saved.getTaskStartDate());
        response.setTaskEndDate(saved.getTaskEndDate());
        response.setCreatedAt(saved.getCreatedAt());
        response.setCreatedBy(saved.getCreatedBy());
        return response;
    }
    
    public ViewReportResponse updateReport(ViewReportRequest request) {
        ViewReports report = viewReportRepository.findById(request.getViewReportId())
            .orElseThrow(() -> new RuntimeException("Report not found"));

        WeeklySummary summary = weeklySummaryRepository.findById(request.getWeekId())
            .orElseThrow(() -> new RuntimeException("Weekly summary not found"));
        Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findByEmployeeId(request.getEmployeeId())
        	    .orElseThrow(() -> new RuntimeException("User not found"));

        report.setTaskName(request.getTaskName());
        report.setTaskStatus(request.getTaskStatus());
        report.setTaskStartDate(request.getTaskStartDate());
        report.setTaskEndDate(request.getTaskEndDate());
        report.setComments(request.getComments());
        report.setProject(project);
        report.setUser(user);
        report.setWeeklySummary(summary);
        report.setTask(new Task(request.getSummary(), request.getKeyAccomplishment()));

        ViewReports updated = viewReportRepository.save(report);

        ViewReportResponse response = new ViewReportResponse();
        response.setViewReportId(updated.getViewReportId());
        response.setTaskName(updated.getTaskName());
        response.setTaskStatus(updated.getTaskStatus());
        response.setSummary(updated.getTask().getSummary());
        response.setKeyAccomplishment(updated.getTask().getKeyAccomplishment());
        response.setComments(updated.getComments());
        response.setProjectName(updated.getProject().getProjectName());
        response.setEmployeeName(updated.getUser().getFirstName() + " " + updated.getUser().getLastName());
        response.setTaskStartDate(updated.getTaskStartDate());
        response.setTaskEndDate(updated.getTaskEndDate());
        response.setCreatedAt(updated.getCreatedAt());
        response.setCreatedBy(updated.getCreatedBy());
        return response;
    }
	public ViewReportResponse getReportById(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Report not found"));
		// Map to response (reuse mapping logic)
		ViewReportResponse response = new ViewReportResponse();
		response.setViewReportId(report.getViewReportId());
		response.setTaskName(report.getTaskName());
		response.setTaskStatus(report.getTaskStatus());
		response.setSummary(report.getTask().getSummary());
		response.setKeyAccomplishment(report.getTask().getKeyAccomplishment());
		response.setComments(report.getComments());
		response.setProjectName(report.getProject().getProjectName());
		response.setEmployeeName(report.getUser().getFirstName() + " " + report.getUser().getLastName());
		response.setTaskStartDate(report.getTaskStartDate());
		response.setTaskEndDate(report.getTaskEndDate());
		response.setCreatedAt(report.getCreatedAt());
		response.setCreatedBy(report.getCreatedBy());
		return response;
	}

	public List<ViewReportResponse> getAllReports() {
		List<ViewReports> reports = viewReportRepository.findAll();
		List<ViewReportResponse> responses = new ArrayList<>();
		for (ViewReports report : reports) {
			ViewReportResponse response = new ViewReportResponse();
			response.setViewReportId(report.getViewReportId());
			response.setTaskName(report.getTaskName());
			response.setTaskStatus(report.getTaskStatus());
			response.setSummary(report.getTask().getSummary());
			response.setKeyAccomplishment(report.getTask().getKeyAccomplishment());
			response.setComments(report.getComments());
			response.setProjectName(report.getProject().getProjectName());
			response.setEmployeeName(report.getUser().getFirstName() + " " + report.getUser().getLastName());
			response.setTaskStartDate(report.getTaskStartDate());
			response.setTaskEndDate(report.getTaskEndDate());
			response.setCreatedAt(report.getCreatedAt());
			response.setCreatedBy(report.getCreatedBy());
			responses.add(response);
		}
		return responses;
	}
	
	 // SOFT DELETE
		public ViewReports softDeleteSummery(Integer viewReportId) {
			ViewReports viewReports = viewReportRepository.findById(viewReportId)
					.orElseThrow(() -> new ResourceNotFoundException("viewReportId not found"));
			viewReports.setSoftDelete(true);
			return viewReportRepository.save(viewReports);
		}

	public void deleteReport(Integer id) {
		ViewReports report = viewReportRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Report not found"));
		viewReportRepository.delete(report);
	}

}
