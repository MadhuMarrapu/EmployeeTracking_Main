package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;
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
		validateSprintDates(request.getFromDate(), request.getToDate());

		Sprint sprint = new Sprint();
		sprint.setSprintNumber(request.getSprintNumber());
		sprint.setSprintName(request.getSprintName());
		sprint.setFromDate(request.getFromDate());
		sprint.setToDate(request.getToDate());
		sprint.setStatusFlag(StatusFlag.ACTIVE);
		sprint.setEnableStatus(EnableStatus.DISABLED);

		List<WeekRange> generatedWeeks = generateWeekRanges(request.getFromDate(), request.getToDate(), sprint);
		sprint.setWeeks(generatedWeeks);

		Sprint savedSprint = sprintRepository.save(sprint);
		return mapToResponse(savedSprint);
	}

	private void validateSprintDates(LocalDate fromDate, LocalDate toDate) {
		LocalDate today = LocalDate.now();
		if (fromDate.isBefore(today)) {
			throw new IllegalArgumentException("From date must be today or a future date.");
		}
		if (toDate.isBefore(today)) {
			throw new IllegalArgumentException("To date must be today or a future date.");
		}
		if (toDate.isBefore(fromDate)) {
			throw new IllegalArgumentException("To date must be after or equal to From date.");
		}
	}

	@Override
	public Page<SprintResponse> getAllSprints(Pageable pageable) {
		return sprintRepository.findByStatusFlag(StatusFlag.ACTIVE, pageable).map(this::mapToResponse);
	}

	@Override
	public SprintResponse getSprintById(Long id) {
		Sprint sprint = getActiveSprintById(id);
		return mapToResponse(sprint);
	}

	@Override
	public SprintResponse updateSprint(Long id, SprintRequest request) {
		 Sprint sprint = sprintRepository.findById(id)
				.orElseThrow(() -> new SprintNotFoundException("Active sprint not found with id: " + id));
		sprint.setSprintNumber(request.getSprintNumber());
		sprint.setSprintName(request.getSprintName());
		sprint.setFromDate(request.getFromDate());
		sprint.setToDate(request.getToDate());
		Sprint updated = sprintRepository.save(sprint);
		return mapToResponse(updated);
	}

	@Override
	public void deleteSprint(Long id) {
		Sprint sprint = sprintRepository.findById(id)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + id));
		sprint.setStatusFlag(StatusFlag.INACTIVE);
		sprintRepository.save(sprint);
	}

	@Override
	public EnableStatus toggleSprintEnabled(Long sprintId) {
		Sprint sprint = sprintRepository.findById(sprintId)
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + sprintId));
		EnableStatus newStatus = EnableStatus.ENABLED;
		sprint.setEnableStatus(newStatus);
		sprintRepository.save(sprint);
		return newStatus;
	}

	@Override
	public Sprint getPreviousSprint(Long sprintId) {
		Sprint currentSprint = getActiveSprintById(sprintId);
		List<Sprint> previousSprints = sprintRepository.findPreviousActiveSprints(currentSprint.getFromDate());
		return previousSprints.stream().filter(s -> s.getStatusFlag() == StatusFlag.ACTIVE).findFirst().orElseThrow(
				() -> new SprintNotFoundException("No active previous sprint found before ID: " + sprintId));
	}

	private List<WeekRange> generateWeekRanges(LocalDate start, LocalDate end, Sprint sprint) {
		List<WeekRange> weekRanges = new ArrayList<>();
		LocalDate expectedEnd = start.plusDays(20); // max 3 weeks (21 days)
		if (end.isAfter(expectedEnd)) {
			end = expectedEnd;
		}
		for (int i = 0; i < 3; i++) {
			LocalDate weekStart = start.plusDays(i * 7);
			LocalDate weekEnd = weekStart.plusDays(6);
			if (weekEnd.isAfter(end)) {
				weekEnd = end;
			}
			WeekRange week = new WeekRange();
			week.setWeekFromDate(weekStart);
			week.setWeekToDate(weekEnd);
			week.setStatusFlag(StatusFlag.ACTIVE);
			week.setEnableStatus(EnableStatus.DISABLED);
			week.setSprint(sprint);
			weekRanges.add(week);
		}
		return weekRanges;
	}

	private SprintResponse mapToResponse(Sprint sprint) {
		List<WeekRangeResponse> weekResponses = new ArrayList<>();

		if (sprint.getWeeks() != null) {
			for (WeekRange week : sprint.getWeeks()) {
				if (week.getStatusFlag() == StatusFlag.ACTIVE) {
					weekResponses.add(new WeekRangeResponse(week.getWeekId(), week.getWeekFromDate(),
							week.getWeekToDate(), week.getStatusFlag(), week.getEnableStatus()));
				}
			}
		}

		return new SprintResponse(sprint.getSprintId(), sprint.getSprintNumber(), sprint.getSprintName(),
				sprint.getFromDate(), sprint.getToDate(), weekResponses, sprint.getStatusFlag(),
				sprint.getEnableStatus());
	}

	private Sprint getActiveSprintById(Long id) {
		return sprintRepository.findById(id).filter(s -> s.getStatusFlag() == StatusFlag.ACTIVE)
				.orElseThrow(() -> new SprintNotFoundException("Active sprint not found with id: " + id));
	}
}