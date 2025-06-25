package com.sam.SamOutlet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findFirstByEmail(String s);
	
	User getUserById(Long id);

	List<User> findByUserRole(UserRole userRole);

	public User findByResetToken(String token);
}
