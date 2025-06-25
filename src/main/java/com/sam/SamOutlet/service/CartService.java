package com.sam.SamOutlet.service; 

import java.util.List;

import com.sam.SamOutlet.dto.CartDTO;
import com.sam.SamOutlet.entities.Cart;

public interface CartService {

	public String saveCart(CartDTO cartItem);
	
	public List<Cart> getCartItems(Long userId) ;
	
	public Long getCountCart(Long userId);

	public void updateQuantity(String sy, Long cid);

}

