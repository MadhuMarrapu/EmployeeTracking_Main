package com.qentelli.employeetrackingsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.qentelli.employeetrackingsystem.entity.Project;
import com.qentelli.employeetrackingsystem.entity.Roles;
import com.qentelli.employeetrackingsystem.entity.User;
import com.qentelli.employeetrackingsystem.exception.InvalidInputDataException;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.request.ProjectDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.request.UserDetailsDto;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;
import com.qentelli.employeetrackingsystem.models.client.response.ProjectDtoResponse;
import com.qentelli.employeetrackingsystem.models.client.response.UserDtoResponse;
import com.qentelli.employeetrackingsystem.repository.UserRepository;

import jakarta.transaction.Transactional;



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
	
	@Transactional
	public UserDtoResponse registerNewUser(UserDetailsDto userDetailsDto) {

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

	    // Parse role
	    Roles role;
	    try {
	        role = Roles.valueOf(userDetailsDto.getRoles().toUpperCase());
	    } catch (IllegalArgumentException ex) {
	        throw new InvalidInputDataException(HttpStatus.BAD_REQUEST,
	                "Invalid role provided: " + userDetailsDto.getRoles() + ". Valid roles: EMPLOYEE, MANAGER, SUPERADMIN");
	    }

	    // Create User
	    User user = new User();
	    user.setFirstName(userDetailsDto.getFirstName());
	    user.setLastName(userDetailsDto.getLastName());
	    user.setEmployeeId(userDetailsDto.getEmployeeId());
	    user.setEmail(userDetailsDto.getEmail());
	    user.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
	    user.setConfirmPassword(passwordEncoder.encode(userDetailsDto.getConfirmPassword()));
	    user.setRoles(role);
	    user.setTechStack(userDetailsDto.getTechStack());

	    // Convert ProjectDetailsDto to Project
	    List<Project> projectEntities = new ArrayList<>();
	    if (userDetailsDto.getProjects() != null) {
	        for (ProjectDetailsDto dto : userDetailsDto.getProjects()) {
	            Project project = new Project();
	            project.setProjectName(dto.getProjectName());
	            project.setLocation(dto.getLocation());
	            project.setStartDate(dto.getStartDate());
	            project.setEndDate(dto.getEndDate());
	            project.setActive(true);
	            project.setUser(user); // link back
	            projectEntities.add(project);
	        }
	    }
	    user.setProjects(projectEntities);

	    // Save user and cascade projects
	    user = userRepository.save(user);

	    // Map to ProjectDtoResponse
	    List<ProjectDtoResponse> projectDtos = user.getProjects().stream().map(project -> {
	        ProjectDtoResponse dto = new ProjectDtoResponse();
	        dto.setProjectId(project.getProjectId());
	        dto.setProjectName(project.getProjectName());
	        dto.setLocation(project.getLocation());
	        dto.setStartDate(project.getStartDate());
	        dto.setEndDate(project.getEndDate());
	        dto.setActive(project.isActive());
	        dto.setCreatedAt(project.getCreatedAt());
	        dto.setCreatedBy(project.getCreatedBy());
	        dto.setUpdatedAt(project.getUpdatedAt());
	        dto.setUpdatedBy(project.getUpdatedBy());
	        return dto;
	    }).collect(Collectors.toList());

	    // Prepare UserDtoResponse
	    UserDtoResponse userDto = new UserDtoResponse();
	    userDto.setId(user.getId());
	    userDto.setFirstName(user.getFirstName());
	    userDto.setLastName(user.getLastName());
	    userDto.setEmployeeId(user.getEmployeeId());
	    userDto.setEmail(user.getEmail());
	    userDto.setRoles(user.getRoles().name());
	    userDto.setProjects(projectDtos);
	    userDto.setTechStack(user.getTechStack());
	    userDto.setCreatedAt(user.getCreatedAt());
	    userDto.setCreatedBy(user.getCreatedBy());
	    userDto.setUpdatedAt(user.getUpdatedAt());
	    userDto.setUpdatedBy(user.getUpdatedBy());

	    return userDto;
	}
	
	public LoginUserResponse loginByEmail(LoginUserRequest loginUser) {
		try {
			String email = loginUser.getEmail();
			String password = loginUser.getPassword();
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
			LoginUserResponse loginUserData = new LoginUserResponse();
			// password check
			user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User not found wih email " + email));
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new BadCredentialsException("Invalid user email or password");
			}
			
			loginUserData.setEmail(user.getEmail());
		//	loginUserData.setPassword(password);
			loginUserData.setRole(user.getRoles().name());
			loginUserData.setAcessToken(accessToken);
			
			return loginUserData;
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid user email or password");
		}
	}

}
