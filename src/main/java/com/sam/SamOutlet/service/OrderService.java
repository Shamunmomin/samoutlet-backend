package com.sam.SamOutlet.service;

import java.util.List;

import com.sam.SamOutlet.dto.OrderRequestDTO;
import com.sam.SamOutlet.entities.ProductOrder;

public interface OrderService {

	public void saveOrder(Long userid, OrderRequestDTO orderRequestDTO);
	
	public void byNowSaveOrder(Long productId,String size,Long userId, OrderRequestDTO orderRequestDTO);
	
	public List<ProductOrder> getOrderByUser(Long userId);
	
	public Boolean updateOrderStatus(String orderId, String status);
	
	public List<ProductOrder> getAllOrders();

	public ProductOrder getOrdersByOrderId(String orderId);
}
