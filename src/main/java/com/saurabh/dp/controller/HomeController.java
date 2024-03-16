package com.saurabh.dp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saurabh.dp.model.UserDtls;
import com.saurabh.dp.service.UserService;

import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

// Users ki url Mapping ke liye isse Use Krenge
// Koun se url pr koun sa page reload hona chahiye

@Controller
public class HomeController {
    
	@Autowired
	private UserService userService;
	
	/* ========== FOR PASSWORD VALIDATION =============== */
    private boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(pattern, password);
    }
	
	// mapping root url / to view index
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	
	// mapping url /signin to view login
	@GetMapping("/signin")
	public String login() {
		
		return "login";
	}
	
	// mapping url /register to view register
	@GetMapping("/register")
	public String register() {
		
		return "register";
	}
	
	// mapping url /createUser to method createUser
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, HttpSession session) {
		
		// ModelAttribute binding the form data to the UserDtls object
	     
		String password = user.getPassword();
		//String confirmPassword = request.getParameter("confirmPassword");)

		
		if(!isValidPassword(password)) {
            session.setAttribute("msg", "Invalid Password type. Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 special character, 1 digit, and be at least 8 characters long.");
            return "redirect:/register";
		}
		
		// Checking if email exists or not
		boolean f = userService.checkEmail(user.getEmail());
		
		if(f) {
			session.setAttribute("msg", "Email id already exists");
		//	System.out.println("Email id already exists");
		}
		else {
			
		
		//System.out.println(user);
		
		// Creating a new user with the provided user details 
		UserDtls userDtls = userService.createUser(user);
		if(userDtls != null) {
			session.setAttribute("msg", "Register Successfully");
			//System.out.println("Register Successfully");
		}
		else {
			session.setAttribute("msg", "Something wrong on server");
			// System.out.println("Something wrong on server");
		}
		 }
		
		
		return "redirect:/register";
	}
	
}