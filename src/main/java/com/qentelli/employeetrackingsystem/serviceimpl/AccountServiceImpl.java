package com.qentelli.employeetrackingsystem.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Account;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.enums.Status;
import com.qentelli.employeetrackingsystem.exception.AccountNotFoundException;
import com.qentelli.employeetrackingsystem.exception.DuplicateAccountException;
import com.qentelli.employeetrackingsystem.models.client.request.AccountDetailsDto;
import com.qentelli.employeetrackingsystem.repository.AccountRepository;
import com.qentelli.employeetrackingsystem.repository.ProjectRepository;
import com.qentelli.employeetrackingsystem.service.AccountService;
import com.qentelli.employeetrackingsystem.service.PersonService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_FOUND = "Account not found with id: ";

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final PersonService personService;
    private final Map<String, Map<String, String>> adminMetadata;
    private final ModelMapper modelMapper;

    @Override
    public Account createAccount(AccountDetailsDto dto) {
        if (accountRepository.existsByAccountName(dto.getAccountName())) {
            throw new DuplicateAccountException("An account with this name already exists.");
        }
        Account account = modelMapper.map(dto, Account.class);
        account.setStatus(Status.ACTIVE); 
        return accountRepository.save(account);
    }

    @Override
    public List<AccountDetailsDto> getAllAccounts() {
        return accountRepository.findByStatus(Status.ACTIVE).stream()
                .map(account -> modelMapper.map(account, AccountDetailsDto.class))
                .toList();
    }

    @Override
    public Page<AccountDetailsDto> getAllActiveAccounts(Pageable pageable) {
        return accountRepository.findByStatus(Status.ACTIVE, pageable)
                .map(account -> modelMapper.map(account, AccountDetailsDto.class));
    }

    @Override
    public Account updateAccount(Integer id, AccountDetailsDto dto) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND + id));
        modelMapper.map(dto, existingAccount);
        existingAccount.setAccountName(dto.getAccountName());
        existingAccount.setAccountStartDate(dto.getAccountStartDate());
        existingAccount.setAccountEndDate(dto.getAccountEndDate());
        existingAccount.setUpdatedAt(LocalDateTime.now());
        existingAccount.setUpdatedBy(getAuthenticatedUserFullName());
        return accountRepository.save(existingAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND + id));
        if (account.getProjects() != null) {
            for (Project project : account.getProjects()) {
                project.setStatusFlag(Status.INACTIVE); 
                project.setUpdatedAt(LocalDateTime.now());
                project.setUpdatedBy(getAuthenticatedUserFullName());
                projectRepository.save(project);
            }
        }
        account.setStatus(Status.INACTIVE);
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(getAuthenticatedUserFullName());
        accountRepository.save(account);
    }

    @Override
    public Page<Account> searchAccountsByExactName(String name, Pageable pageable) {
        return accountRepository.findByAccountNameContainingIgnoreCase(name, pageable);
    }

    private String getAuthenticatedUserFullName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            if (adminMetadata.containsKey(username)) {
                Map<String, String> meta = adminMetadata.get(username);
                return meta.getOrDefault("firstName", "Unknown") + " " + meta.getOrDefault("lastName", "User");
            }
            Person person = personService.getPersonEntity(username);
            if (person != null) {
                return person.getFirstName() + " " + person.getLastName();
            }
        }
        return "System";
    }
}