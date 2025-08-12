package com.qentelli.employeetrackingsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeekRange;

@Repository
public interface WeekRangeRepository extends JpaRepository<WeekRange, Integer> {
	
	public Page<WeekRange> findByWeekFromDateBetweenAndSoftDeleteFalse(LocalDate weekFromDate, LocalDate weekToDate, Pageable pageable);
	public boolean existsByIdAndSoftDelete(Integer id, Boolean softDelete);
	@Query("SELECT w.weekId FROM WeekRange w WHERE w.softDelete = false")
	public List<Integer> findActiveWeekIds();
}