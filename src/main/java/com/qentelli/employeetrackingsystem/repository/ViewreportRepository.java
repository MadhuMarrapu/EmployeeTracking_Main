package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.ViewReports;

@Repository

public interface ViewreportRepository extends JpaRepository<ViewReports, Integer> {

	@Query("SELECT v FROM ViewReports v WHERE v.softDelete = false AND v.weekRange.weekFromDate = :fromDate AND v.weekRange.weekToDate = :toDate")
	List<ViewReports> findByWeekRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
	
	@Query("SELECT v FROM ViewReports v WHERE v.softDelete = false AND "
			+ "(LOWER(v.person.firstName) LIKE LOWER(CONCAT('%', :personName, '%')) "
			+ "OR LOWER(v.person.lastName) LIKE LOWER(CONCAT('%', :personName, '%')))")
	List<ViewReports> searchByPersonName(@Param("personName") String personName);


	@Query("SELECT v FROM ViewReports v WHERE v.softDelete = false AND "
			+ "LOWER(v.project.projectName) LIKE LOWER(CONCAT('%', :projectName, '%'))")
	List<ViewReports> searchByProjectName(@Param("projectName") String projectName);

	@Query("SELECT v FROM ViewReports v WHERE v.softDelete = false AND "
			+ "(LOWER(v.person.firstName) LIKE LOWER(CONCAT('%', :personName, '%')) "
			+ "OR LOWER(v.person.lastName) LIKE LOWER(CONCAT('%', :personName, '%'))) "
			+ "AND LOWER(v.project.projectName) LIKE LOWER(CONCAT('%', :projectName, '%'))")
	List<ViewReports> searchByPersonAndProject(@Param("personName") String personName,
			@Param("projectName") String projectName);
			
	
	//List<ViewReports> findByProject_ProjectName(String projectName);
}
