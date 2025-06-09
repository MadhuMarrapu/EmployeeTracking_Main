package com.qentelli.employeetrackingsystem.repository;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklySummaryRepository extends JpaRepository<WeeklySummary, Long> {
}
