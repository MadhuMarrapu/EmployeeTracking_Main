package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.PIStanding;
@Repository
public interface PIStandingRepository extends JpaRepository<PIStanding, Long> {

	public List<PIStanding> findBySoftDeleteFalse();
	public Page<PIStanding> findBySoftDeleteFalse(Pageable pageable);
	public Page<PIStanding> findByPiNumberAndSoftDeleteFalse(int piNumber, Pageable pageable);
	public Page<PIStanding> findByProject_ProjectIdAndSoftDeleteFalse(int projectId, Pageable pageable);
}
