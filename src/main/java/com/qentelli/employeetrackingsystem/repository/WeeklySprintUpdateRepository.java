package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

@Repository
public interface WeeklySprintUpdateRepository extends JpaRepository<WeeklySprintUpdate, Integer> {

	Page<WeeklySprintUpdate> findByWeeklySprintUpdateStatus(Status statusFlag, Pageable pageable);

	List<WeeklySprintUpdate> findByWeeklySprintUpdateStatus(Status statusFlag);

	@Query("""
			    SELECT wsu FROM WeeklySprintUpdate wsu
			    WHERE wsu.week.weekId = :weekId
			    AND wsu.weeklySprintUpdateStatus = :statusFlag
			""")
	List<WeeklySprintUpdate> findByWeekIdAndStatusFlag(@Param("weekId") int weekId,
			@Param("statusFlag") Status statusFlag);

	@Query("""
			    SELECT wsu FROM WeeklySprintUpdate wsu
			    WHERE wsu.week.sprint.sprintId = :sprintId
			    AND wsu.weeklySprintUpdateStatus = :statusFlag
			""")
	List<WeeklySprintUpdate> findBySprintIdAndStatusFlag(@Param("sprintId") Long sprintId,
			@Param("statusFlag") Status statusFlag);

	@Query("""
			    SELECT w FROM WeeklySprintUpdate w
			    WHERE w.week.weekId IN :weekIds
			    AND w.weeklySprintUpdateStatus = :statusFlag
			""")
	List<WeeklySprintUpdate> findByWeekIdsAndStatusFlag(@Param("weekIds") List<Integer> weekIds,
			@Param("statusFlag") Status statusFlag);
}