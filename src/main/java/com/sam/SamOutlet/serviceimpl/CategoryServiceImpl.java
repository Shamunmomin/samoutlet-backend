package com.sam.SamOutlet.serviceimpl;

 

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sam.SamOutlet.entities.Category;
import com.sam.SamOutlet.repository.CategoryRepository;
import com.sam.SamOutlet.service.CategoryService;

import io.jsonwebtoken.io.IOException;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private final String uploadDir = "uploads";

	
	// SAVE CATEGORY
	public String saveImage(MultipartFile file,Category category) throws IOException {
      
		if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            return "Category with name '" + category.getName() + "' already exists.";
        }
		
		// Create upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Create unique file name and save
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        try {
			Files.write(filePath, file.getBytes());
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && originalFileName.contains(".")) {
            originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        }
        
        // Save metadata to DB
        Category cat = new Category();
        cat.setName(category.getName());
        cat.setIsActive(category.getIsActive());
        cat.setType(file.getContentType());
        cat.setFilePath(filePath.toString());
        
        categoryRepository.save(cat);

        return "Image saved successfully.. ";
    }


	

	// GET ALL CATEGORIES FROM DB
	
	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}



// UPDATE CATEGORY USING ID
	@Override
	public String updateCategoryy(long id, MultipartFile file, Category category) {
		String msg=null;
		 Category cat = categoryRepository.findById(id);
		 if (category.getName() != null) cat.setName(category.getName());
		    if (category.getIsActive() != null) cat.setIsActive(category.getIsActive());
		    if (category.getFilePath() != null) cat.setFilePath(category.getFilePath());
		    
		    if (file != null && !file.isEmpty()) {
		    	
//		      delete existing category image in folder 
		    	 if (cat.getFilePath() != null) {
		             File oldFile = new File(cat.getFilePath());
		             if (oldFile.exists()) {
		                 oldFile.delete(); // 🧹 delete old file
		             }
		    	 }
		        // Save new file
		        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		        Path filePath = Paths.get("uploads", fileName);
		        try {
		            Files.write(filePath, file.getBytes());
		            cat.setFilePath(filePath.toString()); // Update file path in DB
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (java.io.IOException e) {
					e.printStackTrace();
				}
		    }
		    categoryRepository.save(cat);
		    msg="category save successfully..";
		return msg;
	} 


// GET PARTICULAR CATEGORY 
	@Override
	public Category getParticularCategory(long id) {
		Category cat=categoryRepository.findById(id);
		return cat;
	}


//	DELETE PARTICULAR CATEGORY
@Override
public String deleteCategory(long id) {
	Category category=categoryRepository.findById(id);
	String msg=null;
	
	if(Objects.isNull(category)) {
		 msg = "Category not found!";
	}else {
		//  Delete the associated file first
        if (category.getFilePath() != null) {
            File file = new File(category.getFilePath());
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Image file deleted successfully.");
                } else {
                    System.out.println("Failed to delete image file.");
                }
            }
        }
        //Now delete the category from DB
        categoryRepository.delete(category);
        msg = "Category deleted successfully.";
	}
	
	return msg;
}

}
