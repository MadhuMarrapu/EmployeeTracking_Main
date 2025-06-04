package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.entity.User;
import com.qentelli.employeetrackingsystem.exception.InvalidInputDataException;
import com.qentelli.employeetrackingsystem.models.client.request.UserDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.response.UserDto;
import com.qentelli.employeetrackingsystem.repository.UserRepository;



@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}
	
	public UserDto registerNewUser(UserDetailsDto userDetailsDto) {
	    // Check if user already exists
	    Optional<User> userExist = userRepository.findByEmail(userDetailsDto.getEmail());
	    if (userExist.isPresent()) {
	        throw new InvalidInputDataException(HttpStatus.CONFLICT,
	                "Provided user already registered. Please login to continue");
	    }

	    // Check if passwords match
	    if (!userDetailsDto.getPassword().equals(userDetailsDto.getConfirmPassword())) {
	        throw new InvalidInputDataException(HttpStatus.BAD_REQUEST, "Password and Confirm Password do not match!");
	    }

	    // Create new user entity
	    User user = new User(
	            userDetailsDto.getFirstName(),
	            userDetailsDto.getLastName(),
	            userDetailsDto.getEmployeeId(),
	            userDetailsDto.getEmail(),
	            passwordEncoder.encode(userDetailsDto.getPassword())
	    );

	    // Handle roles from JSON without DB
	    Set<String> strRoles = userDetailsDto.getRoles();
	    Set<Roles> roles = new HashSet<>();

	    if (strRoles == null || strRoles.isEmpty()) {
	        roles.add(Roles.EMPLOYEE); // default role
	    } else {
	        for (String roleName : strRoles) {
	            try {
	                Roles role = Roles.valueOf(roleName.toUpperCase());
	                roles.add(role);
	            } catch (IllegalArgumentException ex) {
	                throw new InvalidInputDataException(HttpStatus.BAD_REQUEST,
	                        "Invalid role provided: " + roleName + ". Valid roles: EMPLOYEE, MANAGER, SUPERADMIN");
	            }
	        }
	    }

	    user.setRoles(roles); // Assuming your User model supports EnumSet or similar
	    userRepository.save(user);

	    // Prepare DTO for response
	    UserDto userDto = new UserDto();
	    userDto.setFirstName(user.getFirstName());
	    userDto.setLastName(user.getLastName());
	    userDto.setEmployeeId(user.getEmployeeId());
	    userDto.setEmail(user.getEmail());

	    return userDto;
	}

	public UserDetailsDto loginByEmail(UserDetailsDto userDetailsDto) {
		try {
			String email = userDetailsDto.getEmail();
			String password = userDetailsDto.getPassword();
			// 1. Authenticate
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			// 2. Set authenticated user in the context
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// 3. Get authenticated User object
			User user = (User) authentication.getPrincipal();
			// 4. Generate JWT token
			String accessToken = jwtUtil.generateToken(email);

			// 5. Prepare and return DTO (customize as needed)
			UserDetailsDto userDetails = new UserDetailsDto();
			userDetails.setFirstName(user.getFirstName());
			userDetails.setLastName(user.getLastName());

			// password check
			user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User not found wih email " + email));
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new BadCredentialsException("Invalid user email or password");
			}
			userDetails.setEmployeeId(user.getEmployeeId());
			userDetails.setEmail(email);
			userDetails.setAcessToken(accessToken);
			return userDetails;
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid user email or password");
		}
	}

}
