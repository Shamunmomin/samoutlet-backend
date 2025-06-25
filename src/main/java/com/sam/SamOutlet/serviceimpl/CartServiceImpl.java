package com.sam.SamOutlet.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sam.SamOutlet.dto.CartDTO;
import com.sam.SamOutlet.entities.Cart;
import com.sam.SamOutlet.entities.Product;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.repository.CartRepository;
import com.sam.SamOutlet.repository.ProductRepository;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public String saveCart(CartDTO cartItem) {
		Long userId=cartItem.getUserId();
		Long productId=cartItem.getProductId();
		String size = cartItem.getSize();
		
		
		User user=userRepository.findById(userId).get(); 
		Product product= productRepository.findById(productId).get();
		
		Cart cartStatus= cartRepository.findByProductIdAndUserId(productId, userId);
		
		Cart cart=null;
		String msg=null;
		
		if(ObjectUtils.isEmpty(cartStatus)) {
			cart= new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setSize(size);
			cart.setTotalPrice(1*product.getDiscountPrice());
		}else {
			cart=cartStatus;
			cart.setQuantity(cart.getQuantity()+1);
			cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountPrice());
		}
		
		Cart saveCart=cartRepository.save(cart);
		if(!Objects.isNull(saveCart)) {
			msg="product sucesfully add to cart";
		}else {
			msg="something wrong while adding product to the cart";
		}
		return msg;
	}

	@Override
	public List<Cart> getCartItems(Long userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);

		Double totalOrderPrice = 0.0;
		List<Cart> updateCarts = new ArrayList<>();
		for (Cart c : carts) {
			Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
			c.setTotalPrice(totalPrice);
			totalOrderPrice = totalOrderPrice + totalPrice;
			c.setTotalOrderPrice(totalOrderPrice);
			updateCarts.add(c);
		}
		return updateCarts;
	}

	@Override
	public Long getCountCart(Long userId) {
		 Long countByUserId= cartRepository.countByUserId(userId);
		return countByUserId;
	}

	@Override
	public void updateQuantity(String sy, Long cid) {

		Cart cart=cartRepository.findBycid(cid);
		int updateQuantity;
		if (sy.equalsIgnoreCase("de")) {
			updateQuantity = cart.getQuantity() - 1;

			if (updateQuantity <= 0) {
				cartRepository.delete(cart);
			} else {
				cart.setQuantity(updateQuantity);
				cartRepository.save(cart);
			}

		} else {
			updateQuantity = cart.getQuantity() + 1;
			cart.setQuantity(updateQuantity);
			cartRepository.save(cart);
		}
		
	}


}