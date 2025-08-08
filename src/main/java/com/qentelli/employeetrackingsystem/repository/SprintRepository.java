package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Sprint;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
	public Page<Sprint> findBySprintStatusTrue(Pageable pageable);
}
