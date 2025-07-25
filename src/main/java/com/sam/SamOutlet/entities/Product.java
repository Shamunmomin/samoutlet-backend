package com.sam.SamOutlet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	@Column(length = 5000)
	private String description;
	
	private String categoryName;
	
	private String stock;
	
	private double price;
	
	private double discount;
	
	private double discountPrice;
	
	private String imagePath;
	
	

}
