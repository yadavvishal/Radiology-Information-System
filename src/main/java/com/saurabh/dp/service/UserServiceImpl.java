package com.saurabh.dp.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.saurabh.dp.repository.*;
import com.saurabh.dp.model.UserDtls;

// Implementing the methods of UserService here

@Service
public class UserServiceImpl implements UserService {
    
	// To communicate with the database
	@Autowired
	private UserRepository userRepo;
	
	
	// For password encryption
	@Autowired
	private BCryptPasswordEncoder passwordEncode;  
	
	// New user is created here
	
	@Override
	public UserDtls createUser(UserDtls user) {
		
		
		user.setPassword(passwordEncode.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		
		return userRepo.save(user);
	}
    
	 // Checking the email in the db
	@Override
	public boolean checkEmail(String email) {
		// TODO Auto-generated method stub
		
		return userRepo.existsByEmail(email);
	}
    
	
}

