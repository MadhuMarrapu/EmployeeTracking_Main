package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.repository.WeeklySummaryRepository;

@Service
public class WeeklySummaryService {

	private final WeeklySummaryRepository weeklySummaryRepository;

	public WeeklySummaryService(WeeklySummaryRepository weeklySummaryRepository) {
		this.weeklySummaryRepository = weeklySummaryRepository;
	}

	public List<WeeklySummary> getAllSummaries() {
		return weeklySummaryRepository.findAll();
	}

	public Optional<WeeklySummary> getSummaryById(int id) {
		return weeklySummaryRepository.findById(id);
	}

	public WeeklySummary createSummary(WeeklySummary summary) {
		summary.setCreatedAt(LocalDateTime.now());
		return weeklySummaryRepository.save(summary);
	}

	public WeeklySummary updateSummary(int id, WeeklySummary summary) {
		summary.setWeekId(id);
		summary.setUpdatedAt(LocalDateTime.now());
		return weeklySummaryRepository.save(summary);
	}

	public void deleteSummary(int id) {
		weeklySummaryRepository.deleteById(id);
	}
}