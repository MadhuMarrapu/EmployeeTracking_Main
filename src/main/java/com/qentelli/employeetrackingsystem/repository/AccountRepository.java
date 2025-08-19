package com.qentelli.employeetrackingsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.enums.Status;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsByAccountName(String accountName);
    Page<Account> findByAccountNameContainingIgnoreCase(String accountName, Pageable pageable);
    Page<Account> findByStatus(Status status, Pageable pageable);
    List<Account> findByStatus(Status status);
}