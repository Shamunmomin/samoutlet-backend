package com.sam.SamOutlet.dto;

import com.sam.SamOutlet.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {

	private String jwtToken;
	 private Long userId;
	    private UserRole userRole;
	    private String message;

	
 
	 public AuthenticationResponse(String jwt, Long userId, UserRole userRole) {
	        this.jwtToken = jwt;
	        this.userId = userId;
	        this.userRole = userRole;
	        this.message = null;
	    }
	 
	  public AuthenticationResponse(String message) {
		  this.jwtToken = null;
	        this.userId = null;
	        this.userRole = null;
	        this.message = message;
	    }
	
}
