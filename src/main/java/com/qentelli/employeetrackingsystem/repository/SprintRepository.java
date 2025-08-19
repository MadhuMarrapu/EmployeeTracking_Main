package com.qentelli.employeetrackingsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Sprint;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
	Page<Sprint> findByStatusFlag(StatusFlag statusFlag, Pageable pageable); // ✅ replaces Boolean query

	@Query("SELECT s FROM Sprint s WHERE s.fromDate < :currentFromDate AND s.statusFlag = 'ACTIVE' ORDER BY s.fromDate DESC")
	List<Sprint> findPreviousActiveSprints(@Param("currentFromDate") LocalDate currentFromDate); // ✅ enum-based filter

}
