package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.mapper.AccountMapper;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	// CREATE
	public Account createAccount(AccountDetailsDto accountDetailsDto) {
		Account account = AccountMapper.toEntity(accountDetailsDto);
		return accountRepository.save(account);
	}

	// READ ALL
	public List<AccountDetailsDto> getAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		return accounts.stream().map(AccountMapper::toDto).toList();
	}

	// READ BY ID
	public AccountDetailsDto getAccountById(Integer id) {
		Optional<Account> optionalAccount = accountRepository.findById(id);
		if (!optionalAccount.isPresent()) {
			throw new AccountNotFoundException("Account not found with id: " + id);
		}
		return AccountMapper.toDto(optionalAccount.get());
	}

	// FULL UPDATE
	public Account updateAccount(Integer id, AccountDetailsDto dto) {
		Optional<Account> optionalAccount = accountRepository.findById(id);
		if (!optionalAccount.isPresent()) {
			throw new AccountNotFoundException("Account not found with id: " + id);
		}

		Account existingAccount = optionalAccount.get();

		existingAccount.setAccountName(dto.getAccountName());
		existingAccount.setAccountStartDate(dto.getAccountStartDate());
		existingAccount.setAccountEndDate(dto.getAccountEndDate());
		existingAccount.setCreatedAt(dto.getCreatedAt());
		existingAccount.setCreatedBy(dto.getCreatedBy());
		existingAccount.setUpdatedAt(dto.getUpdatedAt());
		existingAccount.setUpdatedBy(dto.getUpdatedBy());
		existingAccount.setProjects(dto.getProjects());

		return accountRepository.save(existingAccount);
	}

	// PARTIAL UPDATE
	public Account partialUpdateAccount(Integer id, AccountDetailsDto dto) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

		if (dto.getAccountName() != null) {
			account.setAccountName(dto.getAccountName());
		}
		if (dto.getAccountStartDate() != null) {
			account.setAccountStartDate(dto.getAccountStartDate());
		}
		if (dto.getAccountEndDate() != null) {
			account.setAccountEndDate(dto.getAccountEndDate());
		}
		if (dto.getCreatedAt() != null) {
			account.setCreatedAt(dto.getCreatedAt());
		}
		if (dto.getCreatedBy() != null) {
			account.setCreatedBy(dto.getCreatedBy());
		}
		if (dto.getUpdatedAt() != null) {
			account.setUpdatedAt(dto.getUpdatedAt());
		}
		if (dto.getUpdatedBy() != null) {
			account.setUpdatedBy(dto.getUpdatedBy());
		}
		if (dto.getProjects() != null) {
			account.setProjects(dto.getProjects());
		}
		
		return accountRepository.save(account);
	}

	// SOFT DELETE
	public Account softDeleteAccount(Integer id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		account.setSoftDelete(true);
		return accountRepository.save(account);
	}

	// HARD DELETE
	public Account deleteAccount(Integer id) {
		Optional<Account> optionalAccount = accountRepository.findById(id);
		if (!optionalAccount.isPresent()) {
			throw new AccountNotFoundException("Account not found with id: " + id);
		}
		Account account = optionalAccount.get();
		accountRepository.deleteById(id);
		return account;
	}
}
