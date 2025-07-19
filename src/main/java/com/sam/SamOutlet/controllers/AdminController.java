package com.sam.SamOutlet.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sam.SamOutlet.dto.ProductDTO;
import com.sam.SamOutlet.dto.SignupDTO;
import com.sam.SamOutlet.dto.UserDTO;
import com.sam.SamOutlet.entities.Category;
import com.sam.SamOutlet.entities.Product;
import com.sam.SamOutlet.entities.ProductOrder;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.OrderStatus;
import com.sam.SamOutlet.enums.UserRole;
import com.sam.SamOutlet.repository.UserRepository;
import com.sam.SamOutlet.service.CartService;
import com.sam.SamOutlet.service.CategoryService;
import com.sam.SamOutlet.service.OrderService;
import com.sam.SamOutlet.service.ProductService;
import com.sam.SamOutlet.service.UserService;
import com.sam.SamOutlet.utils.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
//	FORGOT PASSWORD
	@PostMapping("/forgot-password")
	public boolean ProcessForgotPassword(@RequestParam String email) throws UnsupportedEncodingException, MessagingException{
		
	 User existingUser = userService.getUserByEmail(email);
	    if(ObjectUtils.isEmpty(existingUser)) {
	    	return false;
	    }else {
	    	String resetToken =UUID.randomUUID().toString();
	    	userService.updateUserResetToken(email,resetToken);
	    	
	    	String url=  "http://localhost:4200/reset-password?token="+resetToken;
	    	
	      Boolean sendMail =commonUtil.sendMail(url,email);
	      
	      if(sendMail) {
	    	  return true;
	      }
	      else {
	    	  return false;
	      }
	    }
		
	}
	
	
//	 CHECK RESET PASSWORD URL
	@GetMapping("/reset-password-link")
	public boolean CheckResetPasswordLink(@RequestParam String token){
		User userByToken= userService.getUserByToken(token);
		
		if(userByToken == null) {
			return false;
		}
		return true;
	}
	
//	RESET PASSWORD 
	@PostMapping("/reset-password")
	public boolean resetPassword(@RequestParam String token, @RequestParam String password) {

		User userByToken = userService.getUserByToken(token);
		if (userByToken == null) {
			return false;
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
			return true;
		}

	}

//	SAVE CATEGORY
	 @PostMapping("/saveCategory")
	    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
	    		                                 @RequestParam("name") String name
	    	                                    , @RequestParam("isActive") Boolean isActive) {
	        try {
	        	 Category category = new Category();
	             category.setName(name);
	             category.setIsActive(isActive);
	            String message = categoryService.saveImage(file,category);
	            return ResponseEntity.ok(message);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
	        }
	    }
	 
//GET ALL CATEGORY FROM DB
	 
	 @GetMapping("/getallcategories")
	 public ResponseEntity<List<Category>>  getAllCategories(){
		 List<Category> categories = categoryService.getAllCategories();
		    return ResponseEntity.ok(categories);
	 }
	 
//	 CATEGORY FIND BY ID
	 @GetMapping("/getParticularCategory/{id}")
		public ResponseEntity<Category> getParticularCategory(@PathVariable long id)
		{
		Category category=categoryService.getParticularCategory(id);
			return ResponseEntity.ok(category);
		}
	 
//	 UPDATE CATEGORY USING ID
	 
	 @PutMapping("/updateCategory/{id}")
	 public ResponseEntity<String> updateCategory( @PathVariable Long id,
			    @RequestPart("file") MultipartFile file,
			    @RequestPart("categoryData") Category category){
		 String msg=categoryService.updateCategoryy(id, file,category);
		return ResponseEntity.ok(msg);
	 }
	 
//  DELETE PARTICULAR CATEGORY
	 @DeleteMapping("/deleteCategory/{id}")
	 public ResponseEntity<String> deleteCategory(@PathVariable long id){
		String msg= categoryService.deleteCategory(id);
		return ResponseEntity.ok(msg);
	 }

	 
	 // ADD PRODUCT 
	 @PostMapping("/saveProduct")
	 public ResponseEntity<String> saveProduct(@RequestParam ("file") MultipartFile file, @ModelAttribute ProductDTO dto){
		 String msg=productService.saveProduct(file, dto);
			if(Objects.isNull(msg)) {
				msg="product not save!";
			}else {
				msg="product save successfully..";
			}
		 return ResponseEntity.ok(msg);
	 }
	 
	 // GET ALL PRODUCTS
	 @GetMapping("/allProduct")
	 public ResponseEntity<List<Product>> getAllProduct(){
		 
		 List<Product> products= productService.getAllProduct();
	
		return ResponseEntity.ok(products);
	 }
	 
//	 UPDATE PRODUCT
	 @PutMapping("/updateProduct/{id}")
	 public ResponseEntity<String> updateProduct(@PathVariable Long id,
			                                     @RequestPart("file") MultipartFile file,
			                                     @RequestPart("productDTO") ProductDTO productDTO ){
		 
		 String msg=productService.updateProduct(id,file,productDTO);
		 
		 return ResponseEntity.ok(msg);
	 }
	 
	 //GET PARTICULAR PRODUCT
	 @GetMapping("/getParticularProduct/{id}")
	 public ResponseEntity<Product> getProductById(@PathVariable long id){
		 Product product= productService.getParticularProduct(id);
		return ResponseEntity.ok(product);
	 }
	 
	 // DELETE PARTICULAR PRODUCT
	 @DeleteMapping("/deleteProduct/{id}")
	 public ResponseEntity<String> deleteProduct(@PathVariable long id){
		 String msg=productService.deleteProduct(id);
		 return ResponseEntity.ok(msg);
	 }
	 
	 //get related product
	 @GetMapping("/relatedProduct/{id}")
	 public ResponseEntity<List<Product>> getRelatedProducts(@PathVariable long id){
		 
		 List<Product> relatedProducts= productService.getRelatedProducts(id);
		return ResponseEntity.ok(relatedProducts);
	 }
	 
	 //GET USER BY USERNAME
	 @GetMapping("/getUser/{id}")
	 public ResponseEntity<User> getParticularUser(@PathVariable Long id){
	     User user = userService.getUserByUserId(id);
	     if (user != null) {
	         return ResponseEntity.ok(user);
	     } else {
	         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	     }
	 }
	 
	 // GET ALL ORDERS
	 @GetMapping("/allOrders")
	 public ResponseEntity<List<ProductOrder>> allOrders(){
		 List<ProductOrder> orders=orderService.getAllOrders();
		return ResponseEntity.ok(orders);
	 }
 
	 // UPDATE ORDER STATUS
	 @PostMapping("/update-Order-Status")
	 public ResponseEntity<String> updateOrderStatus(@RequestParam String id, @RequestParam Integer st) {
	     OrderStatus[] values = OrderStatus.values();
	     String status = null;
	     String msg = null;

	     for (OrderStatus orderSt : values) {
	         if (orderSt.getId().equals(st)) {
	             status = orderSt.getName();
	         }
	     }

	     Boolean updateOrder = orderService.updateOrderStatus(id, status); 
	     if (updateOrder) {
	         msg = "Order Status Updated Successfully..";
	     } else {
	         msg = "Status not updated";
	     }
	     return ResponseEntity.ok(msg);
	 }

	 
//	 GET PRODUCT USING CATEGORYNAME
	 @GetMapping("/getProductsByCategoryname/{categoryName}")
	 public ResponseEntity<List<Product>> getProducts(@PathVariable String categoryName){
		 List<Product> products=productService.getProdutsUsingCategoryName(categoryName);
		 return ResponseEntity.ok(products);
	 }
	 
//	 GET PRODUCT BY PRODUCTID
	 @GetMapping("/search-order/{orderId}")
	 public ResponseEntity<?> searchProduct(@PathVariable String orderId){
		 if (orderId != null && !orderId.trim().isEmpty()) {
			 
	            ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());
	            
	            if (ObjectUtils.isEmpty(order)) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                     .body("Incorrect orderId");
	            } else {
	                return ResponseEntity.ok(order);
	            }
	        } else {
	            List<ProductOrder> allOrders = orderService.getAllOrders();
	            return ResponseEntity.ok(allOrders);
	        }
	    }
	 
//	  ADD ADMIN
	 @PostMapping("/add-admin")
	 public ResponseEntity<?> addAdmin(@RequestBody SignupDTO dto){
		 if(userService.hasUserWithEmail(dto.getEmail())){
			 return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
	                    .body("Admin already exists with this email.");
		 }
		 UserDTO createdAdmin = userService.createAdmin(dto);
	        if (createdAdmin == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Admin not created. Try again later!");
	        }
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmin);
	 }
	 
 // VIEW ALL USERS
	 @GetMapping("/users")
		public ResponseEntity<List<User>> getAllUsers() {
		 UserRole role= UserRole.USER;
			List<User> users = userService.getUsers(role);
			return ResponseEntity.ok(users);
		}
	 
//	  UPDATE USER STATUS ACTIVE OR INACTIVE
	 @GetMapping("/updateSts")
		public boolean updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Long id ) {
			Boolean f = userService.updateAccountStatus(id, status);
			if (f) {
				 return true;
			} else {
				return false;
			}
			
		}
	 
	 // VIEW ALL ADMIN
	 @GetMapping("/admins")
		public ResponseEntity<List<User>> getAllAdmins() {
		 UserRole role= UserRole.ADMIN;
			List<User> users = userService.getUsers(role);
			return ResponseEntity.ok(users);
		}
	 

	 
}
