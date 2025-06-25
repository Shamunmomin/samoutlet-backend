package com.sam.SamOutlet.service;

import java.util.List;

import com.sam.SamOutlet.dto.SignupDTO;
import com.sam.SamOutlet.dto.UserDTO;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.UserRole;

public interface UserService {

	UserDTO createUser(SignupDTO signupDTO);

	boolean hasUserWithEmail(String email);

	User getUserByEmail(String email);
	
	User getUserByUserId(Long id);

	User updateProfile(User user);

	User updateUser(User user);
 
	UserDTO createAdmin(SignupDTO signupDTO);

	List<User> getUsers(UserRole role);

	Boolean updateAccountStatus(Long id, Boolean status);

	void updateUserResetToken(String email, String resetToken);
	
	User getUserByToken(String token);


}
