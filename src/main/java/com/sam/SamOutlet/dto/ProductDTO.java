package com.sam.SamOutlet.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProductDTO {

     private String name;
	
	@Column(length = 5000)
	private String description;
	
	private String categoryName;
	
	private String stock;
	
	private Double price;
	
	private Double discount;
	
	private Double discountPrice;
	
	private String imagePath;
	
	 

}
