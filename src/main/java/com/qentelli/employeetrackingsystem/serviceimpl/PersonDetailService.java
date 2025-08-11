package com.qentelli.employeetrackingsystem.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonDetailService implements UserDetailsService {

	private final PersonRepository personRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return personRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Person not found with email: " + email));
	}

	public Person getPersonEntity(String email) {
		return personRepository.findByEmail(email).orElse(null);
	}
}