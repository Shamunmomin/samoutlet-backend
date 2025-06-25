package com.sam.SamOutlet.serviceimpl;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sam.SamOutlet.dto.SignupDTO;
import com.sam.SamOutlet.dto.UserDTO;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.UserRole;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDTO createUser(SignupDTO signupDTO) {
		 User user=new User();
		 user.setName(signupDTO.getName());
		 user.setEmail(signupDTO.getEmail());
		 user.setUserRole(UserRole.USER);
		 user.setIsEnable(true);
		 user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
		User createdUser= userRepository.save(user);
		 
		UserDTO userDTO= new UserDTO();
		userDTO.setId(createdUser.getId());
		userDTO.setName(createdUser.getName());
		userDTO.setEmail(createdUser.getEmail());
		userDTO.setPassword(createdUser.getPassword());
		userDTO.setUserRole(createdUser.getUserRole());
		userDTO.setIsEnable(createdUser.getIsEnable());
		return userDTO;
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.findFirstByEmail(email)!=null;
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findFirstByEmail(email);
	}

	@Override
	public User getUserByUserId(Long id) {
		 User user= userRepository.getUserById(id);
		return user;
	}

	@Override
	public User updateProfile(User user) {
		User dbUser=userRepository.findById(user.getId()).get();
		if(!ObjectUtils.isEmpty(dbUser)) {
			dbUser.setName(user.getName());
			dbUser.setEmail(user.getEmail());
		    dbUser=userRepository.save(dbUser);
		}
		return dbUser;
	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDTO createAdmin(SignupDTO signupDTO) {
		 User user=new User();
		 user.setName(signupDTO.getName());
		 user.setEmail(signupDTO.getEmail());
		 user.setUserRole(UserRole.ADMIN);
		 user.setIsEnable(true);
		 user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
		User createdUser= userRepository.save(user);
		 
		UserDTO userDTO= new UserDTO();
		userDTO.setId(createdUser.getId());
		userDTO.setName(createdUser.getName());
		userDTO.setEmail(createdUser.getEmail());
		userDTO.setPassword(createdUser.getPassword());
		userDTO.setUserRole(createdUser.getUserRole());
		userDTO.setIsEnable(createdUser.getIsEnable());
		return userDTO;
	}

	@Override
	public List<User> getUsers(UserRole role) {
		return userRepository.findByUserRole(role);
	}

	@Override
	public Boolean updateAccountStatus(Long id, Boolean status) {
		Optional<User> findByuser = userRepository.findById(id);

		if (findByuser.isPresent()) {
			User user = findByuser.get();
			user.setIsEnable(status);
			userRepository.save(user);
			return true;
		}

		return false;
	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {
		User findByemail =userRepository.findFirstByEmail(email);
		findByemail.setResetToken(resetToken);
		userRepository.save(findByemail);
	}

	@Override
	public User getUserByToken(String token) {
		return userRepository.findByResetToken(token);
	}
 
	 
}
