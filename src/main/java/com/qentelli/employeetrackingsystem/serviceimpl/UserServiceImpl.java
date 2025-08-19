package com.qentelli.employeetrackingsystem.serviceimpl;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.config.JwtUtil;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;
import com.qentelli.employeetrackingsystem.service.PersonService;
import com.qentelli.employeetrackingsystem.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final PersonService personService;
	private final Map<String, Map<String, String>> adminMetadata;

	@Override
	public LoginUserResponse loginByEmail(LoginUserRequest loginUser) {
	    String username = loginUser.getUserName();
	    String password = loginUser.getPassword();
	    UsernamePasswordAuthenticationToken authToken =
	        new UsernamePasswordAuthenticationToken(username, password);
	    Authentication authentication = authenticationManager.authenticate(authToken);
	    if (!authentication.isAuthenticated()) {
	        throw new BadCredentialsException("Authentication failed for user: " + username);
	    }
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    String authenticatedUsername = userDetails.getUsername();
	    String token = jwtUtil.generateToken(authenticatedUsername);
	    LoginUserResponse response = new LoginUserResponse();
	    response.setUserName(authenticatedUsername);
	    response.setAcessToken(token);
	    String rawRole = userDetails.getAuthorities().iterator().next().getAuthority();
	    String role = rawRole.startsWith("ROLE_") ? rawRole.substring(5) : rawRole;
	    response.setRole(role);
	    if (adminMetadata.containsKey(authenticatedUsername)) {
	        Map<String, String> meta = adminMetadata.get(authenticatedUsername);
	        response.setFirstName(meta.get("firstName"));
	        response.setLastName(meta.get("lastName"));
	    } else {
	        Person person = personService.getPersonEntity(authenticatedUsername);
	        if (person != null) {
	            response.setFirstName(person.getFirstName());
	            response.setLastName(person.getLastName());
	        } else {
	            response.setFirstName("Unknown");
	            response.setLastName("User");
	        }
	    }
	    return response;
	}
}