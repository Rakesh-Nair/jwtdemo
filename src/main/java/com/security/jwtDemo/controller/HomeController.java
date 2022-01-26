package com.security.jwtDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.jwtDemo.model.JwtRequest;
import com.security.jwtDemo.model.JwtResponse;
import com.security.jwtDemo.service.UserService;
import com.security.jwtDemo.utility.JWTUtility;

@RestController
public class HomeController {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/home")
	public String home() {
		return "Welcome to my Home !! ";
	}

	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest request) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		}
		final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
		final String token = jwtUtility.generateToken(userDetails);
		return new JwtResponse(token);
	}
}
