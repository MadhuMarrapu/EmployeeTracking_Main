package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.WeekRange;
import com.qentelli.employeetrackingsystem.entity.enums.EnableStatus;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.exception.MissingRequestDateException;
import com.qentelli.employeetrackingsystem.exception.SprintNotFoundException;
import com.qentelli.employeetrackingsystem.exception.WeekRangeNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.WeekRangeRequest;
import com.qentelli.employeetrackingsystem.models.client.response.WeekRangeResponse;
import com.qentelli.employeetrackingsystem.repository.SprintRepository;
import com.qentelli.employeetrackingsystem.repository.WeekRangeRepository;
import com.qentelli.employeetrackingsystem.service.WeekRangeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeekRangeServiceImpl implements WeekRangeService {

	private static final String WEEK_RANGE_NOT_FOUND = "WeekRange not found with ID: ";
	private static final String SPRINT_NOT_FOUND = "Sprint not found with ID: ";
	private final WeekRangeRepository weekRangeRepository;
	private final SprintRepository sprintRepository;

	@Override
	public void saveWeeklyData(WeekRangeRequest request) {
		LocalDate currentDate = request.getWeekFromDate();
		Sprint sprint = sprintRepository.findById(request.getSprintId())
				.orElseThrow(() -> new SprintNotFoundException(SPRINT_NOT_FOUND + request.getSprintId()));
		while (!currentDate.isAfter(request.getWeekToDate())) {
			LocalDate weekStart = currentDate.with(DayOfWeek.MONDAY);
			LocalDate weekEnd = weekStart.plusDays(4);
			WeekRange weekRange = new WeekRange();
			weekRange.setWeekFromDate(weekStart);
			weekRange.setWeekToDate(weekEnd);
			weekRange.setStatusFlag(Status.ACTIVE);
			weekRange.setEnableStatus(EnableStatus.DISABLED);
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
				.findActiveWeeksInRange(adjustedWeekFromDate, weekToDate, Pageable.unpaged()).getContent();
		List<WeekRangeResponse> filteredResponses = weekRanges.stream().filter(w -> w.getStatusFlag() == Status.ACTIVE)
				.map(w -> new WeekRangeResponse(w.getWeekId(), w.getWeekFromDate(), w.getWeekToDate(),
						w.getStatusFlag(), w.getEnableStatus()))
				.toList();
		return new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
	}

	@Override
	public WeekRangeResponse getById(int id) {
		WeekRange weekRange = weekRangeRepository.findById(id).filter(w -> w.getStatusFlag() == Status.ACTIVE)
				.orElseThrow(() -> new WeekRangeNotFoundException(WEEK_RANGE_NOT_FOUND + id));
		return new WeekRangeResponse(weekRange.getWeekId(), weekRange.getWeekFromDate(), weekRange.getWeekToDate(),
				weekRange.getStatusFlag(), weekRange.getEnableStatus());
	}

	@Override
	public void softDelete(int id) {
		WeekRange range = weekRangeRepository.findById(id)
				.orElseThrow(() -> new WeekRangeNotFoundException(WEEK_RANGE_NOT_FOUND + id));
		range.setStatusFlag(Status.INACTIVE);
		weekRangeRepository.save(range);
	}

	@Transactional
	@Override
	public boolean setWeekRangeEnabled(Integer id) {
		WeekRange weekRange = weekRangeRepository.findById(id)
				.orElseThrow(() -> new WeekRangeNotFoundException(WEEK_RANGE_NOT_FOUND + id));
		weekRange.setEnableStatus(EnableStatus.ENABLED);
		weekRangeRepository.save(weekRange);
		return true;
	}
}