
package com.saurabh.dp.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.saurabh.dp.model.UserDtls;

//  Implementation of the User Details interface provided by the Spring Security.
public class CustomUserDetails implements UserDetails{
    
	private UserDtls user;
	
	// Storing the user details 
	public CustomUserDetails(UserDtls user) {
		  super();
		  this.user = user;
	}
	
	// All the below are the methods of UserDetails 

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		// Fetching the details about the role of user
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
	
		return user.getPassword();
	}

	@Override
	public String getUsername() {

		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
	
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@Override
	public boolean isEnabled() {
	
		return true;
	}
	
}

// We are here basically extracting the  user information for authentication and authorization purposes  */
