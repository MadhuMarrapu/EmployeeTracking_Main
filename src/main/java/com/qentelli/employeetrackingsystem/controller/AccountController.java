package com.qentelli.employeetrackingsystem.controller;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;
import com.qentelli.employeetrackingsystem.serviceImpl.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDetailsDto dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @GetMapping
    public ResponseEntity<List<AccountDetailsDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailsDto> getAccountById(@PathVariable int id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable int id, @RequestBody AccountDetailsDto dto) {
        return ResponseEntity.ok(accountService.updateAccount(id, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> partialUpdateAccount(@PathVariable int id, @RequestBody AccountDetailsDto dto) {
        return ResponseEntity.ok(accountService.partialUpdateAccount(id, dto));
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Account> softDeleteAccount(@PathVariable int id) {
        return ResponseEntity.ok(accountService.softDeleteAccount(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable int id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }
}