package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.config.JwtUtil;
import com.qentelli.employeetrackingsystem.entity.AuthUserDetails;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.entity.User;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;
import com.qentelli.employeetrackingsystem.repository.PersonRepository;
import com.qentelli.employeetrackingsystem.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    return userRepository.findByUserName(username)
	        .map(UserDetails.class::cast)
	        .orElseGet(() -> personRepository.findByEmail(username)
	            .map(person -> new AuthUserDetails(person.getEmail(), person.getPassword(), person.getRole().name()))
	            .orElseThrow(() -> new UsernameNotFoundException("No User or Person found for username: " + username))
	        );
	}

	public LoginUserResponse loginByEmail(LoginUserRequest loginUser) {
	    String userName = loginUser.getUserName();
	    String password = loginUser.getPassword();

	    try {
	        Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(userName, password)
	        );
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        String accessToken = jwtUtil.generateToken(userName);

	        Optional<User> optionalUser = userRepository.findByUserName(userName);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            return buildResponse(user.getFirstName(), user.getLastName(), user.getUsername(), user.getRoles().name(), accessToken);
	        }

	        Optional<Person> optionalPerson = personRepository.findByEmail(userName);
	        if (optionalPerson.isPresent()) {
	            Person person = optionalPerson.get();
	            if (!Boolean.TRUE.equals(person.getPersonStatus())) {
	                throw new BadCredentialsException("Person account is inactive");
	            }
	            return buildResponse(person.getFirstName(), person.getLastName(), person.getEmail(), person.getRole().name(), accessToken);
	        }

	        throw new BadCredentialsException("No User or Person found for: " + userName);

	    } catch (AuthenticationException e) {
	        throw new BadCredentialsException("Authentication failed for: " + userName);
	    }
	}

	private LoginUserResponse buildResponse(String firstName, String lastName, String userName, String role, String token) {
	    LoginUserResponse response = new LoginUserResponse();
	    response.setFirstName(firstName);
	    response.setLastName(lastName);
	    response.setUserName(userName);
	    response.setRole(role);
	    response.setAcessToken(token);
	    return response;
	}
}
