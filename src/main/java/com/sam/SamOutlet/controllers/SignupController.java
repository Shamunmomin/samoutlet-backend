package com.sam.SamOutlet.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.SamOutlet.dto.SignupDTO;
import com.sam.SamOutlet.dto.UserDTO;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.service.UserService;

@RestController
public class SignupController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) {
        if (userService.hasUserWithEmail(signupDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User already exists with this email.");
        }

        UserDTO createdUser = userService.createUser(signupDTO);
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User not created. Try again later!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
 
}
