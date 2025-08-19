package com.qentelli.employeetrackingsystem.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeeklySummary;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface WeeklySummaryRepository extends JpaRepository<WeeklySummary, Integer> {
    Optional<WeeklySummary> findByWeekStartDateAndWeekEndDate(LocalDate startDate, LocalDate endDate);

    Page<WeeklySummary> findAllByStatusFlag(StatusFlag statusFlag, Pageable pageable);

    Page<WeeklySummary> findByWeekStartDateBetweenAndStatusFlag(LocalDate start, LocalDate end, StatusFlag statusFlag, Pageable pageable);
}