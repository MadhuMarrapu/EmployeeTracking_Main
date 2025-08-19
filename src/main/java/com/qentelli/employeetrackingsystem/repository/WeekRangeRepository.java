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
import com.qentelli.employeetrackingsystem.entity.enums.Status;

@Repository
public interface WeekRangeRepository extends JpaRepository<WeekRange, Integer> {

	@Query("""
			    SELECT w FROM WeekRange w
			    WHERE w.weekFromDate BETWEEN :start AND :end
			    AND w.statusFlag = 'ACTIVE'
			""")
	Page<WeekRange> findActiveWeeksInRange(@Param("start") LocalDate start, @Param("end") LocalDate end,
			Pageable pageable);

	boolean existsByWeekIdAndStatusFlag(Integer weekId, Status statusFlag);

	@Query("SELECT w.weekId FROM WeekRange w WHERE w.statusFlag = 'ACTIVE'")
	List<Integer> findActiveWeekIds();

	@Query("""
			    SELECT w FROM WeekRange w
			    WHERE w.weekId = :weekId
			    AND w.statusFlag = 'ACTIVE'
			""")
	Optional<WeekRange> findActiveById(@Param("weekId") Integer weekId);

	@Query("""
			    SELECT w.weekId FROM WeekRange w
			    WHERE w.sprint.sprintId = :sprintId
			    AND w.statusFlag = 'ACTIVE'
			    AND w.weekId < :currentWeekId
			""")
	List<Integer> findValidHistoricalWeekIds(@Param("sprintId") Long sprintId,
			@Param("currentWeekId") Integer currentWeekId);
}