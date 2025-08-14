package com.qentelli.employeetrackingsystem.serviceimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonDetailService  {

	private final PersonRepository personRepository;

    public UserDetails loadUserDetails(String email) {
        return personRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Person not found with email: " + email));
    }

    public Person getPersonEntity(String email) {
        return personRepository.findByEmail(email).orElse(null);
    }

}