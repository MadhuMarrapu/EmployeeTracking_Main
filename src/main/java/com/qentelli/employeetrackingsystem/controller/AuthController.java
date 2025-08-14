package com.qentelli.employeetrackingsystem.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qentelli.employeetrackingsystem.exception.RequestProcessStatus;
import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.AuthResponse;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;
import com.qentelli.employeetrackingsystem.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse<LoginUserResponse>> loginByUser(
			@Valid @RequestBody LoginUserRequest loginUserRequest) {
		log.info("Login attempt for user: {}", loginUserRequest.getUserName());
		try {
			LoginUserResponse loginuser = userService.loginByEmail(loginUserRequest);
			log.info("Login successful for user: {}", loginUserRequest.getUserName());
			AuthResponse<LoginUserResponse> authResponse = new AuthResponse<>(HttpStatus.OK.value(),
					RequestProcessStatus.SUCCESS, LocalDateTime.now(), "Login successful", loginuser);
			return ResponseEntity.ok(authResponse);
		} catch (BadCredentialsException ex) {
			log.warn("Login failed for user: {} - {}", loginUserRequest.getUserName(), ex.getMessage());
			AuthResponse<LoginUserResponse> errorResponse = new AuthResponse<>(HttpStatus.UNAUTHORIZED.value(),
					RequestProcessStatus.FAILURE, "Invalid credentials", HttpStatus.UNAUTHORIZED,
					"The username or password provided is incorrect");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
}