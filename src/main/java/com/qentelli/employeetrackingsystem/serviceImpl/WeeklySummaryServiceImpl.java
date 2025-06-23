package com.qentelli.employeetrackingsystem.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.repository.WeeklySummaryRepository;

@Service
public class WeeklySummaryServiceImpl  {

    private final WeeklySummaryRepository repository;

    public WeeklySummaryServiceImpl(WeeklySummaryRepository repository) {
        this.repository = repository;
    }

    
    public WeeklySummary createSummary(WeeklySummary summary) {
        summary.setCreatedAt(LocalDateTime.now());
        return repository.save(summary);
    }

   
    public WeeklySummary updateSummary(int id, WeeklySummary updated) {
        updated.setWeekId(id);
        updated.setUpdatedAt(LocalDateTime.now());
        return repository.save(updated);
    }

   
    public List<WeeklySummary> getAllSummaries() {
        return repository.findAll();
    }

 
    public WeeklySummary getSummaryById(int id) {
        return repository.findById(id).orElse(null);
    }

  
    public void deleteSummary(int id) {
        repository.deleteById(id);
    }
}
