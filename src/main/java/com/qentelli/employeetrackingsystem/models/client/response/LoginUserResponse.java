package com.qentelli.employeetrackingsystem.models.client.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {

	private String email;
	private String password;
	private String acessToken;
}
