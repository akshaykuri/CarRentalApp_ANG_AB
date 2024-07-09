package com.codewithprojects.services.auth;

import com.codewithprojects.dto.SignupRequest;
import com.codewithprojects.dto.UserDTO;

public interface AuthService{
	UserDTO createCustomer(SignupRequest signupRequest);
	
	boolean hasCustomerwithEmail(String email);
}