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

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
	public Page<Sprint> findBySprintStatusTrue(Pageable pageable);
	@Query("SELECT s FROM Sprint s WHERE s.fromDate < :currentFromDate AND s.sprintStatus = true ORDER BY s.fromDate DESC")
	List<Sprint> findPreviousActiveSprints(@Param("currentFromDate") LocalDate currentFromDate);
}
