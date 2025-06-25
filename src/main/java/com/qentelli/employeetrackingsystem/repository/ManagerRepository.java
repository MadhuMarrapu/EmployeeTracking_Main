package com.qentelli.employeetrackingsystem.repository;

import com.qentelli.employeetrackingsystem.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    // Add custom queries if needed, like:
    // Optional<Manager> findByEmail(String email);
}