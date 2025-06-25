package com.sam.SamOutlet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sam.SamOutlet.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	boolean existsByNameIgnoreCase(String name);
    
    public Category findById(long id);
}
