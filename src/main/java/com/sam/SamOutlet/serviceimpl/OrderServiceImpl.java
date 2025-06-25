package com.sam.SamOutlet.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sam.SamOutlet.dto.OrderRequestDTO;
import com.sam.SamOutlet.entities.Cart;
import com.sam.SamOutlet.entities.OrderAddress;
import com.sam.SamOutlet.entities.Product;
import com.sam.SamOutlet.entities.ProductOrder;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.OrderStatus;
import com.sam.SamOutlet.repository.CartRepository;
import com.sam.SamOutlet.repository.ProductOrderRepository;
import com.sam.SamOutlet.repository.ProductRepository;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private ProductOrderRepository productOrderRepository;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveOrder(Long userid, OrderRequestDTO orderRequestDTO) {
		List<Cart> carts= cartRepository.findByUserId(userid);
		
		for(Cart cart:carts) {
			ProductOrder order= new ProductOrder();
			
			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(new Date());
			
			order.setProduct(cart.getProduct());
			order.setQuantity(cart.getQuantity());
			order.setPrice(cart.getProduct().getDiscountPrice()*cart.getQuantity());
			order.setSize(cart.getSize());
			order.setUser(cart.getUser());
			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequestDTO.getPaymentType());
			
			OrderAddress address= new OrderAddress();
			address.setFirstName(orderRequestDTO.getFirstName());
			address.setLastName(orderRequestDTO.getLastName());
			address.setEmail(orderRequestDTO.getEmail());
			address.setMobileNo(orderRequestDTO.getMobileNo());
			address.setAddress(orderRequestDTO.getAddress());
			address.setCity(orderRequestDTO.getCity());
			address.setState(orderRequestDTO.getState());
			address.setPincode(orderRequestDTO.getPincode());

			order.setOrderAddress(address);

			productOrderRepository.save(order);
		}
		 
	}
	
	// BUY NOW ORDER SAVE
	public void byNowSaveOrder(Long productId,String size,Long userId, OrderRequestDTO orderRequestDTO) {
		Product product=productRepository.findById(productId).orElse(null);
		User user=userRepository.findById(userId).orElse(null);
		
		ProductOrder order = new ProductOrder();
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderDate(new Date());
		order.setProduct(product);
		order.setPrice(product.getDiscountPrice());
		order.setSize(size);
		order.setQuantity(1);
		order.setUser(user);
		order.setStatus(OrderStatus.IN_PROGRESS.getName());
		order.setPaymentType(orderRequestDTO.getPaymentType());
		
		OrderAddress address= new OrderAddress();
		address.setFirstName(orderRequestDTO.getFirstName());
		address.setLastName(orderRequestDTO.getLastName());
		address.setEmail(orderRequestDTO.getEmail());
		address.setMobileNo(orderRequestDTO.getMobileNo());
		address.setAddress(orderRequestDTO.getAddress());
		address.setCity(orderRequestDTO.getCity());
		address.setState(orderRequestDTO.getState());
		address.setPincode(orderRequestDTO.getPincode());

		order.setOrderAddress(address);
		
		productOrderRepository.save(order);
		
	}

	@Override
	public List<ProductOrder> getOrderByUser(Long userId) {
		 List<ProductOrder> orders=productOrderRepository.findByUserId(userId);
		return orders;
	}

	@Override
	public Boolean updateOrderStatus(String orderId, String status) {
	    Optional<ProductOrder> findByOrderId = Optional.ofNullable(productOrderRepository.findByOrderId(orderId));
	    if (findByOrderId.isPresent()) {
	        ProductOrder productOrder = findByOrderId.get();
	        productOrder.setStatus(status);
	        productOrderRepository.save(productOrder);
	        return true;
	    }
	    return false;
	}

	@Override
	public List<ProductOrder> getAllOrders() {
		return productOrderRepository.findAll();
	}

	@Override
	public ProductOrder getOrdersByOrderId(String orderId) {
		return productOrderRepository.findByOrderId(orderId);
	}


	
	 

}
