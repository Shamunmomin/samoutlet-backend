package com.sam.SamOutlet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.SamOutlet.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	
	public Cart findByProductIdAndUserId(Long productId, Long userID);
	
	public Long countByUserId(Long userId);
	
	public List<Cart> findByUserId(Long userId);
	
	public Cart findBycid(Long cid);
	
	public Cart findByProductId(Long productId);

}
