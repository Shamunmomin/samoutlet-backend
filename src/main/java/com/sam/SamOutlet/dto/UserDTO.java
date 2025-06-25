	package com.sam.SamOutlet.dto;
	
	import com.sam.SamOutlet.enums.UserRole;
	
	import lombok.Data;
	
	@Data
	public class UserDTO {
	
		
		private long id;
		
		private String name;
		
		private String email;
		
		private String password;
		
		private UserRole userRole;
		
		  private Boolean isEnable;	
		
	}
