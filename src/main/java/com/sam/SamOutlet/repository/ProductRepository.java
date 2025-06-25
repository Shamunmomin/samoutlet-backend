package com.sam.SamOutlet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.sam.SamOutlet.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

   public Product findById(long id);
   
   List<Product> findByNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String ch, String ch2);
   
   List<Product> findByCategoryName(String categoryName);

   public List<Product> findByName(String name);
	
}
