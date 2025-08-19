package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.PIStanding;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface PIStandingRepository extends JpaRepository<PIStanding, Long> {

    List<PIStanding> findByStatusFlag(StatusFlag statusFlag);

    Page<PIStanding> findByStatusFlag(StatusFlag statusFlag, Pageable pageable);

    Page<PIStanding> findByPiNumberAndStatusFlag(int piNumber, StatusFlag statusFlag, Pageable pageable);

    Page<PIStanding> findByProject_ProjectIdAndStatusFlag(int projectId, StatusFlag statusFlag, Pageable pageable);
}