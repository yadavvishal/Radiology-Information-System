

package com.saurabh.dp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// To handle all the authentication and authorization process

@Configuration  // Spring Configuration
@EnableWebSecurity // Enabling web security features
public class SecurityConfig extends WebSecurityConfigurerAdapter{
   
	
	// Loading user-specific data during authentication

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	// For password encoding
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		  return new BCryptPasswordEncoder();
	}
	
	// Providing authentication 
	@Bean
	public DaoAuthenticationProvider getDaoAuthProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	// Configuring the custom daoAuthenticationProvider as authentication provider
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(getDaoAuthProvider());
	}


    // Responsible for handling HTTP security configurations
   	@Override
	protected void configure(HttpSecurity http) throws Exception{
		 
		// based on the role permitting the access, matching the url
   		
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		   .antMatchers("/user/**").hasRole("USER")
		   .antMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/login")
		   .defaultSuccessUrl("/user/").and().csrf().disable(); 

	} 
}


