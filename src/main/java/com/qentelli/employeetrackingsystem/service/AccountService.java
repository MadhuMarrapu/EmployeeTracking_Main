package com.qentelli.employeetrackingsystem.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;

public interface AccountService {

	public Account createAccount(AccountDetailsDto dto);
	public List<AccountDetailsDto> getAllAccounts();
	public Page<AccountDetailsDto> getAllActiveAccounts(Pageable pageable);
	public AccountDetailsDto getAccountById(Integer id);
	public Account updateAccount(Integer id, AccountDetailsDto dto);
	public Account partialUpdateAccount(Integer id, AccountDetailsDto dto);
	public Account softDeleteAccount(Integer id);
	public void deleteAccount(Integer id);
	public Page<Account> searchAccountsByExactName(String name, Pageable pageable);
}