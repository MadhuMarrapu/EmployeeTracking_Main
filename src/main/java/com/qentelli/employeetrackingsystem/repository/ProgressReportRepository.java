package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qentelli.employeetrackingsystem.entity.ProgressReport;

public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {
	Page<ProgressReport> findByProgressReportStatusTrue(Pageable pageable);

}