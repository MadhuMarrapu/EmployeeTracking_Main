package com.qentelli.employeetrackingsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qentelli.employeetrackingsystem.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	public boolean existsByAccountName(String accountName);
	public Page<Account> findByAccountNameContainingIgnoreCase(String accountName, Pageable pageable);
	public Page<Account> findByAccountStatusTrue(Pageable pageable);

}
