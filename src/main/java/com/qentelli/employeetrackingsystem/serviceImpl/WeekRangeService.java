package com.qentelli.employeetrackingsystem.serviceImpl;

import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeekRangeService {

    @Autowired
    private WeekRangeRepository weekRangeRepository;

    // Save weekly data in the database
    public void saveWeeklyData(LocalDate weekFromDate, LocalDate weekToDate) {
        LocalDate currentDate = weekFromDate;
        while (!currentDate.isAfter(weekToDate)) {
            LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);
            LocalDate weekEnd = weekStart.plusDays(4); // Exclude Saturday and Sunday

            WeekRange weekRange = new WeekRange();
            weekRange.setWeekFromDate(weekStart);
            weekRange.setWeekToDate(weekEnd);
            weekRange.setSoftDelete(false);

            weekRangeRepository.save(weekRange);

            currentDate = currentDate.plusWeeks(1);
        }
    }

    // Generate report excluding Saturdays and Sundays
    public Page<WeekRangeResponse> generateReport(LocalDate weekFromDate, LocalDate weekToDate, Pageable pageable) {
        // Adjust the weekFromDate to the start of the week (Monday)
        LocalDate adjustedWeekFromDate = weekFromDate.with(DayOfWeek.MONDAY);

        // Fetch all records without pagination
        List<WeekRange> weekRanges = weekRangeRepository.findByWeekFromDateBetweenAndSoftDeleteFalse(adjustedWeekFromDate, weekToDate, Pageable.unpaged()).getContent();

        // Map entities to response objects
        List<WeekRangeResponse> filteredResponses = weekRanges.stream()
                .map(weekRange -> new WeekRangeResponse(
                        weekRange.getId(),
                        weekRange.getWeekFromDate(),
                        weekRange.getWeekToDate()
                ))
                .collect(Collectors.toList());

        // Return paginated response manually
        return new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
    }
}