package com.codewithprojects.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithprojects.dto.AuthenticationRequest;
import com.codewithprojects.dto.AuthenticationResponse;
import com.codewithprojects.dto.SignupRequest;
import com.codewithprojects.dto.UserDTO;
import com.codewithprojects.entity.User;
import com.codewithprojects.repository.UserRepository;
import com.codewithprojects.services.auth.AuthService;
import com.codewithprojects.services.jwt.UserService;
import com.codewithprojects.utils.JWTUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController{
	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest){
		if(authService.hasCustomerwithEmail(signupRequest.getEmail()))
			return new ResponseEntity<>("Customer already exists with this Email",HttpStatus.NOT_ACCEPTABLE);

		UserDTO createdCustomerDTO = authService.createCustomer(signupRequest);
		if(createdCustomerDTO==null){
			return new ResponseEntity<>("Customer not created, Come again later", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>(createdCustomerDTO,HttpStatus.CREATED);
		}
	}

	@PostMapping("/login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException, DisabledException, UsernameNotFoundException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("Incorrect Username or Password!!");
		}

		final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		
		if(optionalUser.isPresent()) {
			authenticationResponse.setJwt(jwt);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setUserRole(optionalUser.get().getUserRole());
		}

		return authenticationResponse;
	}
}