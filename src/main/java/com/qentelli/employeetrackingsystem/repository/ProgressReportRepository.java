package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.ProgressReport;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface ProgressReportRepository extends JpaRepository<ProgressReport, Long> {
    Page<ProgressReport> findByStatusFlag(StatusFlag statusFlag, Pageable pageable); // ✅ updated
}