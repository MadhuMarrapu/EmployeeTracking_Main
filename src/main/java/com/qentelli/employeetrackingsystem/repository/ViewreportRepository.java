package com.qentelli.employeetrackingsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.ViewReports;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface ViewreportRepository extends JpaRepository<ViewReports, Integer> {

	// 🔹 Week range filter with lifecycle awareness
	@Query("SELECT v FROM ViewReports v WHERE v.statusFlag = :statusFlag AND v.weekRange.weekFromDate = :fromDate AND v.weekRange.weekToDate = :toDate")
	List<ViewReports> findByWeekRangeAndStatusFlag(@Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate, @Param("statusFlag") StatusFlag statusFlag);

	// 🔹 Lifecycle-aware fetches
	List<ViewReports> findByStatusFlag(StatusFlag statusFlag);

	Page<ViewReports> findByStatusFlag(StatusFlag statusFlag, Pageable pageable);

	Page<ViewReports> findByStatusFlagAndPerson_PersonId(StatusFlag statusFlag, Integer personId, Pageable pageable);

	Page<ViewReports> findByStatusFlagAndProject_ProjectId(StatusFlag statusFlag, Integer projectId, Pageable pageable);

	Page<ViewReports> findByStatusFlagAndPerson_PersonIdAndProject_ProjectId(StatusFlag statusFlag, Integer personId,
			Integer projectId, Pageable pageable);
}