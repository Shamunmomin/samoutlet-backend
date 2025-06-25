package com.sam.SamOutlet.serviceimpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sam.SamOutlet.dto.ProductDTO;
import com.sam.SamOutlet.entities.Product;
import com.sam.SamOutlet.repository.ProductRepository;
import com.sam.SamOutlet.service.ProductService;

import io.jsonwebtoken.io.IOException;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepository productRepository;

	private final String uploadDir = "uploads/products";

	@Override
	public String saveProduct(MultipartFile file, ProductDTO dto) {

		// Create upload directory if it doesn't exist
		File directory = new File(uploadDir);
		if (!directory.exists()) {
			directory.mkdir();
		}

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(uploadDir, fileName);
		try {
			Files.write(filePath, file.getBytes());
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		Product product= new Product();

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCategoryName(dto.getCategoryName());
		product.setStock(dto.getStock());
		product.setPrice(dto.getPrice());
		product.setDiscount(dto.getDiscount());
		

		double disPrice=dto.getPrice()*dto.getDiscount()/100;

		double mainPrice=dto.getPrice()-disPrice;

		product.setDiscountPrice(mainPrice);
		product.setImagePath("uploads/products/"+fileName);
		 

		productRepository.save(product);

		return "prduct save successfully..";

	}


	//	 GET ALL PRODUCT FROM DB
	@Override
	public List<Product> getAllProduct() {
		List<Product> pp=  productRepository.findAll();
		return pp;
	}



	//	UPDATE PRODUCT 
	@Override
	public String updateProduct(Long id, MultipartFile file, ProductDTO productDTO) {
		String msg=null;

		Product product=getParticularProduct(id);

		if (productDTO.getName() != null) product.setName(productDTO.getName());
		if(productDTO.getDescription() != null) product.setDescription(productDTO.getDescription());
		if(productDTO.getCategoryName() != null) product.setCategoryName(productDTO.getCategoryName());
		if(productDTO.getStock() != null) product.setStock(productDTO.getStock());
		if(productDTO.getPrice() != null) product.setPrice(productDTO.getPrice());
		if(productDTO.getDiscount() != null) product.setDiscount(productDTO.getDiscount());


		double disPrice=productDTO.getPrice()*productDTO.getDiscount()/100;

		double mainPrice=productDTO.getPrice()-disPrice;
		if(productDTO.getDiscountPrice() !=null) product.setDiscountPrice(mainPrice);
		if(productDTO.getImagePath() != null) product.setImagePath(productDTO.getImagePath());
		 
		if (file != null && !file.isEmpty()) {

			//		      delete existing category image in folder 
			if (product.getImagePath() != null) {
				File oldFile = new File(product.getImagePath());
				if (oldFile.exists()) {
					oldFile.delete(); // 🧹 delete old file
				}
			}

			// Save new file
			String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get("uploads/products", fileName);
			try {
				Files.write(filePath, file.getBytes());
				product.setImagePath(filePath.toString()); // Update file path in DB
			} catch (IOException e) {
				e.printStackTrace();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}
		productRepository.save(product);
		msg="product update successfully..";
		return msg;
	}


	//	GET PRODUCT BY ID
	@Override
	public Product getParticularProduct(long id) {
		Product product= productRepository.findById(id);
		return product;
	}


	@Override
	public String deleteProduct(long id) {
		String msg=null;
		Product product= productRepository.findById(id);
		if(Objects.isNull(product)) {
			msg="Product not found ";
		}else {
		//  Delete the associated file first
	        if (product.getImagePath() != null) {
	            File file = new File(product.getImagePath());
	            if (file.exists()) {
	                if (file.delete()) {
	                    System.out.println("Image file deleted successfully.");
	                } else {
	                    System.out.println("Failed to delete image file.");
	                }
	            }
	        }
	        productRepository.delete(product);
	        msg="product deleted succesfully..";
		}
		return msg;
	}


	@Override
	public List<Product> getRelatedProducts(long id) {
		Product existingProduct=productRepository.findById(id);
		 String categoryName = existingProduct.getCategoryName(); 
		
		List<Product> allProducts= productRepository.findAll();
		List<Product> relatedProducts= new ArrayList<Product>();
		
		for(Product product:allProducts) {
			if(product.getCategoryName().toLowerCase().contains(categoryName.toLowerCase())) {
				relatedProducts.add(product);
			}
		}
		 
		return relatedProducts;
	}


	@Override
	public List<Product> searchProduct(String ch) {
		return productRepository.findByNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(ch, ch);
	}


	@Override
	public List<Product> getProdutsUsingCategoryName(String categoryName) {
		return productRepository.findByCategoryName(categoryName);
	}

 

}
