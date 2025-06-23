package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.mapper.AccountMapper;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse2;
import com.qentelli.employeetrackingsystem.models.client.response.MessageResponse;
import com.qentelli.employeetrackingsystem.serviceImpl.AccountService;

@RestController
@RequestMapping("/api")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@PostMapping("/createAccount")
	public ResponseEntity<?> createAccount(@RequestBody AccountDetailsDto accountDetailsDto) {
		try {
			Account account = accountService.createAccount(accountDetailsDto);
			AccountDetailsDto responseDto = AccountMapper.toDto(account);
			AuthResponse2<AccountDetailsDto> authResponse = new AuthResponse2<>(HttpStatus.OK.value(),
					RequestProcessStatus.SUCCESS, "Account created successfully");
			return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatusCode()).body(new MessageResponse(e.getMessage()));
		}
	}

	@GetMapping("/viewAccounts")
	public ResponseEntity<AuthResponse<List<AccountDetailsDto>>> getAllAccounts() {
		List<AccountDetailsDto> accountList = accountService.getAllAccounts();
		AuthResponse<List<AccountDetailsDto>> authResponse = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Accounts fetched successfully", accountList);
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@GetMapping("/viewAccount/{id}")
	public ResponseEntity<AuthResponse<AccountDetailsDto>> getAccountById(@PathVariable Integer id) {
		AccountDetailsDto accountDto = accountService.getAccountById(id);
		AuthResponse<AccountDetailsDto> authResponse = new AuthResponse<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Account fetched successfully", accountDto);
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PutMapping("/updateAccount/{id}")
	public ResponseEntity<?> updateAccount(@PathVariable Integer id, @RequestBody AccountDetailsDto dto) {
		Account updatedAccount = accountService.updateAccount(id, dto);
		AccountDetailsDto responseDto = AccountMapper.toDto(updatedAccount);
		AuthResponse2<AccountDetailsDto> authResponse = new AuthResponse2<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Account updated successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@PatchMapping("/partialUpdateAccount/{id}")
	public ResponseEntity<?> partialUpdateAccount(@PathVariable Integer id, @RequestBody AccountDetailsDto dto) {
		Account updatedAccount = accountService.partialUpdateAccount(id, dto);
		AccountDetailsDto responseDto = AccountMapper.toDto(updatedAccount);
		AuthResponse2<AccountDetailsDto> authResponse = new AuthResponse2<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Account partially updated successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@DeleteMapping("/softDeleteAccount/{id}")
	public ResponseEntity<?> softDeleteAccount(@PathVariable Integer id) {
		accountService.softDeleteAccount(id);
		AuthResponse2<AccountDetailsDto> authResponse = new AuthResponse2<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Account soft deleted successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAccount/{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
		accountService.deleteAccount(id);
		AuthResponse2<AccountDetailsDto> authResponse = new AuthResponse2<>(HttpStatus.OK.value(),
				RequestProcessStatus.SUCCESS, "Account deleted successfully");
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}
}
