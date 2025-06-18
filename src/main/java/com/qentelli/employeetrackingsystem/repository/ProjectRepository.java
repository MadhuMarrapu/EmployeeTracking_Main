package com.qentelli.employeetrackingsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

}
