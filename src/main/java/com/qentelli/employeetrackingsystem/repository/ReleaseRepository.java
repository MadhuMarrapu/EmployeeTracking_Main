package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Release;
import com.qentelli.employeetrackingsystem.entity.ReleaseCountProjection;
import com.qentelli.employeetrackingsystem.entity.enums.StatusFlag;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query(value = """
        SELECT p.project_name AS projectName,
               COALESCE(SUM(r.major), 0) AS totalMajor,
               COALESCE(SUM(r.minor), 0) AS totalMinor,
               COALESCE(SUM(r.incident_created), 0) AS totalIncidents
        FROM project p
        LEFT JOIN release r ON p.project_id = r.project_id
        WHERE r.status_flag = 'ACTIVE'
        GROUP BY p.project_name
        """, nativeQuery = true)
    List<ReleaseCountProjection> getAggregatedReleaseCounts();

    List<Release> findByWeek_WeekId(int weekId);
    List<Release> findBySprint_SprintId(long sprintId);

    // ✅ Lifecycle-aware queries
    List<Release> findByStatusFlag(StatusFlag statusFlag);
    List<Release> findByWeek_WeekIdAndStatusFlag(int weekId, StatusFlag statusFlag);
    List<Release> findBySprint_SprintIdAndStatusFlag(long sprintId, StatusFlag statusFlag);

    // ✅ Paginated lifecycle-aware query
    Page<Release> findByStatusFlag(StatusFlag statusFlag, Pageable pageable);
}