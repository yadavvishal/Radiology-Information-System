package com.saurabh.dp.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/* ============== ONLY FOR TESTING AND SELF PRACTICE TO RE-ROUTE TO ANOTHER APP ================== */

@Controller
@RequestMapping("/tst")
public class SwitchController {
      
	@GetMapping("/")
	public void handleGet(HttpServletResponse response) {
	    response.setHeader("Location", "http://localhost:3000/");
	    response.setStatus(302);
	}
}
