package com.qentelli.employeetrackingsystem.service;

import com.qentelli.employeetrackingsystem.models.client.request.LoginUserRequest;
import com.qentelli.employeetrackingsystem.models.client.response.LoginUserResponse;

public interface UserService {

	public LoginUserResponse loginByEmail(LoginUserRequest loginUser);
}