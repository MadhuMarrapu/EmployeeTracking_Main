package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.exception.MissingRequestDateException;
import com.qentelli.employeetrackingsystem.exception.SprintNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import com.qentelli.employeetrackingsystem.service.WeekRangeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeekRangeServiceImpl implements WeekRangeService {

	private final WeekRangeRepository weekRangeRepository;
	private final SprintRepository sprintRepository;

	@Override
	public void saveWeeklyData(WeekRangeRequest request) {
		LocalDate currentDate = request.getWeekFromDate();
		Sprint sprint = sprintRepository.findById(request.getSprintId())
				.orElseThrow(() -> new SprintNotFoundException("Sprint not found with ID: " + request.getSprintId()));
		while (!currentDate.isAfter(request.getWeekToDate())) {
			LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);
			LocalDate weekEnd = weekStart.plusDays(4);
			WeekRange weekRange = new WeekRange();
			weekRange.setWeekFromDate(weekStart);
			weekRange.setWeekToDate(weekEnd);
			weekRange.setSoftDelete(false);
			weekRange.setSprint(sprint);
			weekRangeRepository.save(weekRange);
			currentDate = currentDate.plusWeeks(1);
		}
	}

	@Override
	public Page<WeekRangeResponse> generateReport(LocalDate weekFromDate, LocalDate weekToDate, Pageable pageable) {
		if (weekFromDate == null || weekToDate == null) {
			throw new MissingRequestDateException("weekFromDate/weekToDate",
					"Both weekFromDate and weekToDate must be provided.");
		}
		if (weekFromDate.isAfter(weekToDate)) {
			throw new MissingRequestDateException("weekFromDate",
					"weekFromDate must be before or equal to weekToDate.");
		}
		LocalDate adjustedWeekFromDate = weekFromDate.with(DayOfWeek.MONDAY);
		List<WeekRange> weekRanges = weekRangeRepository
				.findByWeekFromDateBetweenAndSoftDeleteFalse(adjustedWeekFromDate, weekToDate, Pageable.unpaged())
				.getContent();
		List<WeekRangeResponse> filteredResponses = weekRanges.stream()
				.map(weekRange -> new WeekRangeResponse(weekRange.getWeekId(), weekRange.getWeekFromDate(),
						weekRange.getWeekToDate()))
				.toList();
		return new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
	}

	@Override
	public void softDelete(int id) {
		WeekRange range = weekRangeRepository.findById(id).orElseThrow();
		range.setSoftDelete(true);
		weekRangeRepository.save(range);
	}
}