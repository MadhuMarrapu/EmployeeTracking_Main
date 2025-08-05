package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.qentelli.employeetrackingsystem.entity.PIStanding;

public interface PIStandingRepository extends JpaRepository<PIStanding, Long> {

	List<PIStanding> findBySoftDeleteFalse();

	Page<PIStanding> findBySoftDeleteFalse(Pageable pageable);

	Page<PIStanding> findByPiNumberAndSoftDeleteFalse(int piNumber, Pageable pageable);

	Page<PIStanding> findByProject_ProjectIdAndSoftDeleteFalse(int projectId, Pageable pageable);
}
