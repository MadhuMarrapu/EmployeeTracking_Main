package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.qentelli.employeetrackingsystem.config.JwtUtil;
import com.qentelli.employeetrackingsystem.entity.Person;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;

@Service
public class UserService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PersonService personService;

	@Autowired
	private Map<String, Map<String, String>> adminMetadata;

	public LoginUserResponse loginByEmail(LoginUserRequest loginUser) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = jwtUtil.generateToken(userDetails.getUsername());
		
		LoginUserResponse response = new LoginUserResponse();
		response.setUserName(userDetails.getUsername());
<<<<<<< HEAD
		String rawRole = userDetails.getAuthorities().iterator().next().getAuthority();
		String role = rawRole.startsWith("ROLE_")
		    ? rawRole.substring(5) // removes "ROLE_"
		    : rawRole;
		response.setRole(role); // optional: "superadmin"
=======
		
		
		String role = userDetails.getAuthorities().iterator().next().getAuthority();
	    if (role.startsWith("ROLE_")) {
	        role = role.substring(5); // remove "ROLE_" prefix
	    }
	    response.setRole(role);
>>>>>>> 10fab52800a39454f4652bc1bc13f669d6837c99
		response.setAcessToken(token);

		if (adminMetadata.containsKey(userDetails.getUsername())) {
			Map<String, String> meta = adminMetadata.get(userDetails.getUsername());
			response.setFirstName(meta.get("firstName"));
			response.setLastName(meta.get("lastName"));
		} else {
			Person person = personService.getPersonEntity(userDetails.getUsername());
			if (person != null) {
				response.setFirstName(person.getFirstName());
				response.setLastName(person.getLastName());
<<<<<<< HEAD
				
=======
				String rolePerson = userDetails.getAuthorities().iterator().next().getAuthority();
				rolePerson = rolePerson.replaceFirst("^ROLE_", "").toLowerCase();
			    response.setRole(rolePerson);
>>>>>>> 10fab52800a39454f4652bc1bc13f669d6837c99

			}
		}

		return response;
	}
}
