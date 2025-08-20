package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.exception.SprintNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.SprintRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintResponse;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.service.SprintService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {

	private final SprintRepository sprintRepository;
	private static final String SPRINT_NOT_FOUND = "Sprint not found with id: ";

	@Override
	public SprintResponse createSprint(SprintRequest request) {
		Sprint sprint = new Sprint();
		sprint.setSprintNumber(request.getSprintNumber());
		sprint.setSprintName(request.getSprintName());
		sprint.setFromDate(request.getFromDate());
		sprint.setToDate(request.getToDate());
		List<WeekRange> generatedWeeks = generateWeekRanges(request.getFromDate(), request.getToDate(), sprint);
		sprint.setWeeks(generatedWeeks);
		Sprint savedSprint = sprintRepository.save(sprint);
		List<WeekRangeResponse> weekResponses = savedSprint.getWeeks().stream().filter(week -> !week.getSoftDelete())
				.map(week -> new WeekRangeResponse(week.getWeekId(), week.getWeekFromDate(), week.getWeekToDate()))
				.toList();
		return new SprintResponse(savedSprint.getSprintId(), savedSprint.getSprintNumber(), savedSprint.getSprintName(),
				savedSprint.getFromDate(), savedSprint.getToDate(), weekResponses, savedSprint.getEnabled(),
				savedSprint.getSprintStatus());
	}

	/*
	@Override
	public SprintResponse createSprint1(SprintRequest request) {
		LocalDate today = LocalDate.now();
		if (request.getFromDate().isBefore(today)) {
			throw new IllegalArgumentException("From date must be today or a future date.");
		}
		if (request.getToDate().isBefore(today)) {
			throw new IllegalArgumentException("To date must be today or a future date.");
		}
		if (request.getToDate().isBefore(request.getFromDate())) {
			throw new IllegalArgumentException("To date must be after or equal to From date.");
		}
		Sprint sprint = new Sprint();
		sprint.setSprintNumber(request.getSprintNumber());
		sprint.setSprintName(request.getSprintName());
		sprint.setFromDate(request.getFromDate());
		sprint.setToDate(request.getToDate());
		List<WeekRange> generatedWeeks = generateWeekRanges(request.getFromDate(), request.getToDate(), sprint);
		sprint.setWeeks(generatedWeeks);
		Sprint savedSprint = sprintRepository.save(sprint);
		List<WeekRangeResponse> weekResponses = savedSprint.getWeeks().stream().filter(week -> !week.getSoftDelete())
				.map(week -> new WeekRangeResponse(week.getWeekId(), week.getWeekFromDate(), week.getWeekToDate()))
				.toList();
		return new SprintResponse(savedSprint.getSprintId(), savedSprint.getSprintNumber(), savedSprint.getSprintName(),
				savedSprint.getFromDate(), savedSprint.getToDate(), weekResponses, savedSprint.getEnabled(),
				savedSprint.getSprintStatus());
	}
*/
	@Override
	public Page<SprintResponse> getAllSprints(Pageable pageable) {
		return sprintRepository.findBySprintStatusTrue(pageable).map(this::mapToResponse);
	}

	@Override
	public SprintResponse getSprintById(Long id) {
		Sprint sprint = sprintRepository.findById(id)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + id));
		return mapToResponse(sprint);
	}

	@Override
	public SprintResponse updateSprint(Long id, SprintRequest request) {
		Sprint sprint = sprintRepository.findById(id)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND+ id));
		sprint.setSprintNumber(request.getSprintNumber());
		sprint.setSprintName(request.getSprintName());
		sprint.setFromDate(request.getFromDate());
		sprint.setToDate(request.getToDate());
		Sprint updated = sprintRepository.save(sprint);
		return mapToResponse(updated);
	}


	// SOFT DELETE (mark inactive)
	@Override
	public void deleteSprint(Long id) {
	    Sprint sprint = sprintRepository.findById(id)
	            .orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + id));
	    sprint.setSprintStatus(false); // mark as inactive
	    sprintRepository.save(sprint);
	}
	
	public boolean setSprintEnabled(Long sprintId) {
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + sprintId));
		boolean newStatus = !Boolean.TRUE.equals(sprint.getEnabled());
		sprint.setEnabled(newStatus);
		sprintRepository.save(sprint);
		return newStatus;
	}
	
	@Override
	public Sprint getPreviousSprint(Long sprintId) {
		Sprint currentSprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + sprintId));
		List<Sprint> previousSprints = sprintRepository.findPreviousActiveSprints(currentSprint.getFromDate());
		return previousSprints.stream()
				.findFirst()
				.orElseThrow(() -> new SprintNotFoundException("No previous sprint found before ID: " + sprintId));
	}

	private List<WeekRange> generateWeekRanges(LocalDate start, LocalDate end, Sprint sprint) {
	    List<WeekRange> weekRanges = new ArrayList<>();

	    // Always cap the end date to 21 days from start
	    LocalDate expectedEnd = start.plusDays(20); // 21 days total
	    if (end.isAfter(expectedEnd)) {
	        end = expectedEnd;
	    }

	    for (int i = 0; i < 3; i++) {
	        LocalDate weekStart = start.plusDays(i * 7);
	        LocalDate weekEnd = weekStart.plusDays(6);

	        // If weekEnd goes past allowed end date, trim it
	        if (weekEnd.isAfter(end)) {
	            weekEnd = end;
	        }

	        WeekRange week = new WeekRange();
	        week.setWeekFromDate(weekStart);
	        week.setWeekToDate(weekEnd);
	        week.setSoftDelete(false);
	        week.setEnabled(false); // Ensure DB non-null constraint
	        week.setSprint(sprint);

	        weekRanges.add(week);
	    }

	    return weekRanges;
	}

	
	private SprintResponse mapToResponse(Sprint sprint) {
	    List<WeekRangeResponse> weekResponses = new ArrayList<>();

	    if (sprint.getWeeks() != null) {
	        for (WeekRange week : sprint.getWeeks()) {
	            // Safely check for non-deleted weeks
	            if (Boolean.FALSE.equals(week.getSoftDelete())) {
	                weekResponses.add(new WeekRangeResponse(
	                    week.getWeekId(),
	                    week.getWeekFromDate(),
	                    week.getWeekToDate()
	                ));
	            }
	        }
	    }

	    return new SprintResponse(
	        sprint.getSprintId(),
	        sprint.getSprintNumber(),
	        sprint.getSprintName(),
	        sprint.getFromDate(),
	        sprint.getToDate(),
	        weekResponses,
	        sprint.getSprintStatus(),
	        sprint.getEnabled() // updated to match new field name
	    );
	}
}