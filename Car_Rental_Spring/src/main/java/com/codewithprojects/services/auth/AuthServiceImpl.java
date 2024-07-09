package com.codewithprojects.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.codewithprojects.dto.SignupRequest;
import com.codewithprojects.dto.UserDTO;
import com.codewithprojects.entity.User;
import com.codewithprojects.enums.UserRole;
import com.codewithprojects.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void createAdminAccount() {
		User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
		
		if(adminAccount == null) {
			User newAdminAccount = new User();
			newAdminAccount.setName("Admin");
			newAdminAccount.setEmail("admin@test.com");
			newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("admin"));
			newAdminAccount.setUserRole(UserRole.ADMIN);
			userRepository.save(newAdminAccount);
			System.out.println("Admin Account Created Succesfully!!");
		}
	}
	
	@Override
	public UserDTO createCustomer(SignupRequest signupRequest){
		User user = new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
//signupRequest.getPassword()
		user.setUserRole(UserRole.CUSTOMER);
		User createdUser = userRepository.save(user);
		UserDTO userDTO = new UserDTO();
		userDTO.setId(createdUser.getId());
		return userDTO;
	}

	@Override
	public boolean hasCustomerwithEmail(String email) {
		return userRepository.findFirstByEmail(email).isPresent();
	}
}