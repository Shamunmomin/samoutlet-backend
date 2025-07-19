package com.sam.SamOutlet.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sam.SamOutlet.dto.AuthenticationRequest;
import com.sam.SamOutlet.dto.AuthenticationResponse;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController

public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public static final String TOKEN_PREFIX="Bearer ";
	public static final String HEADER_STRING="Authorization";
 


	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
	        @RequestBody AuthenticationRequest authenticationRequest) {
		
		 User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

		    if (user == null) {
		        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
		                .body(new AuthenticationResponse("User not found.Please create new account!"));
		    }
		    
	    try {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        authenticationRequest.getUsername(),
	                        authenticationRequest.getPassword())
	        );
	    }
	    catch (BadCredentialsException e) {
	        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
	                .body(new AuthenticationResponse("Incorrect Username or Password."));
	    } 
	    catch (DisabledException e) {
	        return ResponseEntity.status(HttpServletResponse.SC_NOT_ACCEPTABLE)  // 406 here
	                .body(new AuthenticationResponse("User is not activated."));
	    }

//	    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
	   

	    final String jwt = jwtUtil.getToken(authenticationRequest.getUsername());

	    AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, user.getId(), user.getUserRole());

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer " + jwt);
	    headers.add("Access-Control-Expose-Headers", "Authorization");

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(authenticationResponse);
	}

}
