package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.models.client.request.SprintRequest;
import com.qentelli.employeetrackingsystem.models.client.response.SprintResponse;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;


@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    public SprintResponse createSprint(SprintRequest request) {
        Sprint sprint = new Sprint();
        sprint.setSprintNumber(request.getSprintNumber());
        sprint.setSprintName(request.getSprintName());
        sprint.setFromDate(request.getFromDate());
        sprint.setToDate(request.getToDate());

        // Generate valid weekday-only weeks
        List<WeekRange> generatedWeeks = generateWeekRanges(
                request.getFromDate(), request.getToDate(), sprint
        );
        sprint.setWeeks(generatedWeeks);

        Sprint savedSprint = sprintRepository.save(sprint);

        List<WeekRangeResponse> weekResponses = savedSprint.getWeeks().stream()
                .filter(week -> !week.isSoftDelete())
                .map(week -> new WeekRangeResponse(
                        week.getWeekId(),
                        week.getWeekFromDate(),
                        week.getWeekToDate()
                ))
                .collect(Collectors.toList());

        return new SprintResponse(
                savedSprint.getSprintId(),
                savedSprint.getSprintNumber(),
                savedSprint.getSprintName(),
                savedSprint.getFromDate(),
                savedSprint.getToDate(),
                weekResponses
        );
    }

    public Page<SprintResponse> getAllSprints(Pageable pageable) {
        return sprintRepository.findAll(pageable).map(this::mapToResponse);
    }

    public SprintResponse getSprintById(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));

        return mapToResponse(sprint);
    }

    public SprintResponse updateSprint(Long id, SprintRequest request) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));

        sprint.setSprintNumber(request.getSprintNumber());
        sprint.setSprintName(request.getSprintName());
        sprint.setFromDate(request.getFromDate());
        sprint.setToDate(request.getToDate());

        Sprint updated = sprintRepository.save(sprint);
        return mapToResponse(updated);
    }

    public void deleteSprint(Long id) {
        if (!sprintRepository.existsById(id)) {
            throw new RuntimeException("Sprint not found with id: " + id);
        }

        sprintRepository.deleteById(id);
    }

    // ✅ Generate weekday-only week ranges (no weekends in start or end dates)
    private List<WeekRange> generateWeekRanges(LocalDate start, LocalDate end, Sprint sprint) {
        List<WeekRange> weekRanges = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            // Skip weekends for week start
            while (current.getDayOfWeek() == DayOfWeek.SATURDAY || current.getDayOfWeek() == DayOfWeek.SUNDAY) {
                current = current.plusDays(1);
                if (current.isAfter(end)) return weekRanges;
            }

            LocalDate weekStart = current;
            LocalDate weekEnd = weekStart;
            int workingDays = 1;

            // Accumulate up to 5 working days (Mon–Fri)
            while (workingDays < 5) {
                weekEnd = weekEnd.plusDays(1);
                if (weekEnd.isAfter(end)) break;

                if (weekEnd.getDayOfWeek() != DayOfWeek.SATURDAY && weekEnd.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    workingDays++;
                }
            }

            // Ensure weekEnd does not fall on weekend
            while (weekEnd.getDayOfWeek() == DayOfWeek.SATURDAY || weekEnd.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekEnd = weekEnd.minusDays(1);
            }

            WeekRange week = new WeekRange();
            week.setWeekFromDate(weekStart);
            week.setWeekToDate(weekEnd);
            week.setSoftDelete(false);
            week.setSprint(sprint);

            weekRanges.add(week);

            // Move to next day after this week
            current = weekEnd.plusDays(1);
        }

        return weekRanges;
    }

    // Helper to convert Sprint to SprintResponse
    private SprintResponse mapToResponse(Sprint sprint) {
        List<WeekRangeResponse> weekResponses = new ArrayList<>();
        if (sprint.getWeeks() != null) {
            for (WeekRange week : sprint.getWeeks()) {
                if (!week.isSoftDelete()) {
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
                weekResponses
        );
    }
}