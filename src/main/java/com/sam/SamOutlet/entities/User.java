package com.sam.SamOutlet.entities;
 
import com.sam.SamOutlet.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private UserRole userRole;

    private Boolean isEnable;	 
	 
    private String resetToken;

}
