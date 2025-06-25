package com.qentelli.employeetrackingsystem.models.client.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResponse {

	private String userName;
//	private String password;
	private String role;
	private String acessToken;

}
