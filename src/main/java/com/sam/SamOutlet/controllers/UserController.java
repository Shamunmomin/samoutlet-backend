package com.sam.SamOutlet.controllers;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.SamOutlet.dto.CartDTO;
import com.sam.SamOutlet.dto.OrderRequestDTO;
import com.sam.SamOutlet.entities.Cart;
import com.sam.SamOutlet.entities.Category;
import com.sam.SamOutlet.entities.Product;
import com.sam.SamOutlet.entities.ProductOrder;
import com.sam.SamOutlet.entities.User;
import com.sam.SamOutlet.enums.OrderStatus;
import com.sam.SamOutlet.service.CartService;
import com.sam.SamOutlet.service.CategoryService;
import com.sam.SamOutlet.service.OrderService;
import com.sam.SamOutlet.service.ProductService;
import com.sam.SamOutlet.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody CartDTO cartItem){
		
		String msg= cartService.saveCart(cartItem);
		if(Objects.isNull(msg)) {
			msg="product not add in cart";
		}else {
			msg="product successfully add to cart";
		}
		
		return ResponseEntity.ok(msg);
	}
	
	@GetMapping("/cart/{id}")
	public ResponseEntity<?> cartView( @PathVariable long id){
		
		User user= userService.getUserByUserId(id);
		
		List<Cart> carts= cartService.getCartItems(user.getId());
		 if(carts.isEmpty()) {
			 return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Cart is empty");
		 }
		 
		 Map<String, Object> response = new HashMap<>();
         response.put("carts", carts);
         
         Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
         response.put("totalOrderPrice", totalOrderPrice);
         
		return ResponseEntity.ok(response);
	}
	
	 @GetMapping("/getCount/{id}")
	 public ResponseEntity<Long> getCount(@PathVariable Long id){
		 Long count=cartService.getCountCart(id);
//		 System.out.println(count);
		 return ResponseEntity.ok(count);
	 }
	 
	 @GetMapping("/cartQuantityUpdate")
		public void updateCartQuantity(@RequestParam String sy, @RequestParam Long cid) {
//			System.out.println("sy= "+sy+" cid= "+cid);
			cartService.updateQuantity(sy, cid);
		}

	 @PostMapping("/saveOrder/{userid}")
	 public ResponseEntity<String> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable("userid") Long userid){
		 
//		 User user=userService.getUserByUserId(userid);
		 orderService.saveOrder(userid, orderRequestDTO);
		 String msg="order save succesfully";
		 return ResponseEntity.ok(msg);
	 }
	 
//	 SAVE BY NOW ORDER
	 @PostMapping("/saveByNowOrder")
	 public ResponseEntity<String> saveByNowOrder(@RequestParam Long id, @RequestParam String size,@RequestParam("userId") Long userId  , @RequestBody OrderRequestDTO dto){
		 String msg=null;
		 orderService.byNowSaveOrder(id, size,userId, dto);
		 msg="order save successfully..";
		 return ResponseEntity.ok(msg);
	 }
  
	 @GetMapping("/user-orders/{userId}")
	 public ResponseEntity<List<ProductOrder>> getAllOrders(@PathVariable("userId") Long userId){
		 List<ProductOrder> orders =orderService.getOrderByUser(userId);
		return ResponseEntity.ok(orders);
	 }
	 
	 @GetMapping("/updateOrderStatus")
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

	 // GET USER BY USERID
	 @GetMapping("getParticularUser/{id}")
	 public ResponseEntity<User> getUser(@PathVariable Long id){
		 User user=userService.getUserByUserId(id);
		 return ResponseEntity.ok(user);
	 }

	 //UPDATE USER PROFILE
	 @PostMapping("editProfiel")
	 public ResponseEntity<String> editUserProfile(@RequestBody User user){
		 String msg=null;
		 User u=userService.updateProfile(user);
		 if(Objects.isNull(u)) {
			 msg="user profile is not updated";
		 }else {
			 msg="user profile updated succesfully";
		 }
		 return ResponseEntity.ok(msg);
	 }
	 
	 // CHANGE USER PASSWORD
	 @PostMapping("changePassword/{id}")
	 public ResponseEntity<User> chanePassword(@RequestParam String newPassword, @RequestParam String currentPassword, @PathVariable Long id){
		
		 User user= userService.getUserByUserId(id);
		 boolean matches=passwordEncoder.matches(currentPassword, user.getPassword());
		 
		 if(matches) {
			 String encodePassword = passwordEncoder.encode(newPassword);
			 user.setPassword(encodePassword);
			 User updateUser=userService.updateUser(user);
			 return ResponseEntity.ok(updateUser);
		 }
		 
		 return ResponseEntity.ok(null);
	 }
	 
	 
	 
	 
 }
