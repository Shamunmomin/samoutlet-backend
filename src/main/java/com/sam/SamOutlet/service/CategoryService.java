package com.sam.SamOutlet.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sam.SamOutlet.entities.Category;

public interface CategoryService {

	 
	public String saveImage(MultipartFile file, Category category);
	
	List<Category> getAllCategories();

	public String updateCategoryy(long id,MultipartFile file,Category category);

	public Category getParticularCategory(long id);

	public String deleteCategory(long id);
}
