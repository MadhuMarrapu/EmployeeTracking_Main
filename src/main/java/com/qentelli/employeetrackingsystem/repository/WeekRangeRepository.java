package com.qentelli.employeetrackingsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeekRange;

@Repository
public interface WeekRangeRepository extends JpaRepository<WeekRange, Integer> {

	
	@Query("SELECT w FROM WeekRange w WHERE w.weekFromDate BETWEEN :start AND :end AND w.softDelete = false")
	Page<WeekRange> findActiveWeeksInRange(@Param("start") LocalDate start, @Param("end") LocalDate end,
			Pageable pageable);

	boolean existsByWeekIdAndSoftDelete(Integer weekId, boolean softDelete);

	@Query("SELECT w.weekId FROM WeekRange w WHERE w.softDelete = false")
	public List<Integer> findActiveWeekIds();
	@Query("""
		    SELECT w FROM WeekRange w
		    WHERE w.weekId = :weekId
		    AND w.softDelete = false
		""")
		Optional<WeekRange> findActiveById(@Param("weekId") Integer weekId);
	@Query("""
		    SELECT w.weekId FROM WeekRange w
		    WHERE w.sprint.sprintId = :sprintId
		    AND w.softDelete = false
		    AND w.weekId < :currentWeekId
		""")
		List<Integer> findValidHistoricalWeekIds(@Param("sprintId") Long sprintId, @Param("currentWeekId") Integer currentWeekId);
	
}