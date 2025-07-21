package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeeklySprintUpdate;

@Repository
public interface WeeklySprintUpdateRepository extends JpaRepository<WeeklySprintUpdate, Integer> {
	Page<WeeklySprintUpdate> findByWeeklySprintUpdateStatusTrue(Pageable pageable);
	
	 List<WeeklySprintUpdate> findByWeeklySprintUpdateStatusTrue();
}