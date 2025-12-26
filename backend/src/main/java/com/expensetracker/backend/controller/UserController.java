package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.UserResponse;
import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;
import jakarta.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Register a new user
   @PostMapping("/register")
public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
    User newUser = userService.registerUser(user.getEmail(), user.getPassword());
    
    // Return response without password
    UserResponse response = new UserResponse(
        newUser.getId(),
        newUser.getEmail(),
        newUser.getCreatedAt()
    );
    
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

    // Login user
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
    String email = credentials.get("email");
    String password = credentials.get("password");
    
    User user = userService.login(email, password);
    
    // Return user without password
    Map<String, Object> response = new HashMap<>();
    response.put("id", user.getId());
    response.put("email", user.getEmail());
    response.put("message", "Login successful");
    
    return ResponseEntity.ok(response);
}
    
    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    
    // Get user by ID
   @GetMapping("/{id}")
public ResponseEntity<?> getUserById(@PathVariable Long id) {
    User user = userService.findById(id);
    return ResponseEntity.ok(user);
}
    
    // Get all users (for testing only)
    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}