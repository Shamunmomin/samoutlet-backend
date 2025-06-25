package com.sam.SamOutlet.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sam.SamOutlet.dto.ProductDTO;
import com.sam.SamOutlet.entities.Product;

public interface ProductService {
	
	public String saveProduct(MultipartFile file, ProductDTO dto);

	public List<Product> getAllProduct();

	public String updateProduct(Long id, MultipartFile file, ProductDTO productDTO);

	public Product getParticularProduct(long id);

	public String deleteProduct(long id);

	public List<Product> getRelatedProducts(long id);
	
	public List<Product> searchProduct(String ch);

	public List<Product> getProdutsUsingCategoryName(String categoryName);

}
