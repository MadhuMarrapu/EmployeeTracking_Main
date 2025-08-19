package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Locale;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonDetailService {

	private final PersonRepository personRepository;

	public UserDetails loadUserDetails(String email) throws UsernameNotFoundException {
		UserDetails user = personRepository.findByEmail(email.toLowerCase(Locale.ROOT))
				.orElseThrow(() -> new UsernameNotFoundException("Person not found with email: " + email));
		return wrapIfNeeded(user);
	}

	public Person getPersonEntity(String email) {
		return personRepository.findByEmail(email).orElse(null);
	}

	public static UserDetails wrapIfNeeded(UserDetails user) {
		if (user instanceof CredentialsContainer) {
			return user;
		}
		return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
	}

}