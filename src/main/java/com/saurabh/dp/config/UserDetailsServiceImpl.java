 

package com.saurabh.dp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saurabh.dp.model.UserDtls;
import com.saurabh.dp.repository.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	// To communicate with the database
	
	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
	
		// Fetching the user details based on the email
		UserDtls user = userRepo.findByEmail(email);
		
		// If email exist creating a new object
		if(user != null) {
			
			return new CustomUserDetails(user);
		}
		
		// If email doesn't exist display the following message
		
		throw new UsernameNotFoundException("user not available");
	}
}




