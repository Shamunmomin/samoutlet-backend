package com.sam.SamOutlet.service.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user=userRepository.findFirstByEmail(username);
		if (user == null) throw new UsernameNotFoundException("Username not found: " + username);
	
		 if (Boolean.FALSE.equals(user.getIsEnable())) {
	            throw new DisabledException("User account is disabled.");
	        }
		 
		return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()  // You can populate authorities here if roles are added
        );
	}
	
}
