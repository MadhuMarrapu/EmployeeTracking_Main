package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;

@Service
public class WeekRangeService {

    @Autowired
    private WeekRangeRepository weekRangeRepository;

    public List<WeekRangeResponse> generateWeeks(LocalDate inputDate) {
        LocalDate startMonday = inputDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy");

        List<WeekRangeResponse> responses = new ArrayList<>();

        for (int i = 0; i < 52; i++) {
            LocalDate weekStart = startMonday.plusWeeks(i);
            LocalDate weekEnd = weekStart.plusDays(6);

            String label = "WEEK " + (i + 1) + " : " + weekStart.format(formatter) + " To " + weekEnd.format(formatter);

            WeekRange entity = new WeekRange();
            entity.setRangeLabel(label);
            weekRangeRepository.save(entity);

            responses.add(new WeekRangeResponse(entity.getId(), entity.getRangeLabel()));
        }

        return responses;
    }

    public List<WeekRangeResponse> getAll() {
        return weekRangeRepository.findAll().stream()
                .map(e -> new WeekRangeResponse(e.getId(), e.getRangeLabel()))
                .collect(Collectors.toList());
    }
}
