package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ModelMapper modelMapper;

	// CREATE
	public Account createAccount(AccountDetailsDto dto) {
		Account account = modelMapper.map(dto, Account.class);
		if (dto.getProjects() != null) {
			List<Project> projects = dto.getProjects().stream().map(projectDto -> {
				Project project = modelMapper.map(projectDto, Project.class);
				project.setAccount(account);
				return project;
			}).collect(Collectors.toList());
			account.setProjects(projects);
		}
		return accountRepository.save(account);
	}

	// READ ALL
	public List<AccountDetailsDto> getAllAccounts() {
		return accountRepository.findAll().stream().map(account -> modelMapper.map(account, AccountDetailsDto.class))
				.collect(Collectors.toList());
	}

	// READ BY ID
	public AccountDetailsDto getAccountById(int id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
		return modelMapper.map(account, AccountDetailsDto.class);
	}

	// UPDATE
	public Account updateAccount(int id, AccountDetailsDto dto) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

		modelMapper.map(dto, account);
		return accountRepository.save(account);
	}

	// PARTIAL UPDATE
	public Account partialUpdateAccount(int id, AccountDetailsDto dto) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

		if (dto.getAccountName() != null)
			account.setAccountName(dto.getAccountName());
		if (dto.getAccountStartDate() != null)
			account.setAccountStartDate(dto.getAccountStartDate());
		if (dto.getAccountEndDate() != null)
			account.setAccountEndDate(dto.getAccountEndDate());
		if (dto.getCreatedAt() != null)
			account.setCreatedAt(dto.getCreatedAt());
		if (dto.getCreatedBy() != null)
			account.setCreatedBy(dto.getCreatedBy());
		if (dto.getUpdatedAt() != null)
			account.setUpdatedAt(dto.getUpdatedAt());
		if (dto.getUpdatedBy() != null)
			account.setUpdatedBy(dto.getUpdatedBy());

		return accountRepository.save(account);
	}

	// SOFT DELETE
	public Account softDeleteAccount(int id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found"));
		account.setSoftDelete(true);
		return accountRepository.save(account);
	}

	// HARD DELETE
	public Account deleteAccount(int id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
		accountRepository.delete(account);
		return account;
	}
}