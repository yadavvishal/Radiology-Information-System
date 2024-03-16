package com.saurabh.dp.service;

import com.saurabh.dp.model.*;

//  Methods to be performed
public interface UserService {
    
	public UserDtls createUser(UserDtls user);
	
	public boolean checkEmail(String email);
}
