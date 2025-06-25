package com.sam.SamOutlet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.SamOutlet.entities.ProductOrder;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>{

	List<ProductOrder> findByUserId(Long userId);

//	 Optional<ProductOrder> findByOrderId(String orderId);
 
	 ProductOrder findByOrderId(String orderId);

}
