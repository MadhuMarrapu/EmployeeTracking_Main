package com.qentelli.employeetrackingsystem.serviceImpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.WeekRange;

@Repository
public interface WeekRangeRepository extends JpaRepository<WeekRange, Integer> {
	
}
