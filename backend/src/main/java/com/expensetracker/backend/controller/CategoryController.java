package com.expensetracker.backend.controller;

import com.expensetracker.backend.entity.Category;
import com.expensetracker.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    // Create default categories for a user
    @PostMapping("/default/{userId}")
    public ResponseEntity<?> createDefaultCategories(@PathVariable Long userId) {
            categoryService.createDefaultCategories(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Default categories created");
       
    }
    
    // Create custom category
   @PostMapping("/custom")
public ResponseEntity<?> createCustomCategory(@Valid @RequestBody Map<String, Object> request) {
    String name = (String) request.get("name");
    Long userId = Long.valueOf(request.get("userId").toString());
    
    Category category = categoryService.createCustomCategory(name, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(category);
}
    
    // Get all categories for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Category>> getUserCategories(@PathVariable Long userId) {
        List<Category> categories = categoryService.getUserCategories(userId);
        return ResponseEntity.ok(categories);
    }
    
    // Get only default categories
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<List<Category>> getDefaultCategories(@PathVariable Long userId) {
        List<Category> categories = categoryService.getDefaultCategories(userId);
        return ResponseEntity.ok(categories);
    }
    
    // Get only custom categories
    @GetMapping("/user/{userId}/custom")
    public ResponseEntity<List<Category>> getCustomCategories(@PathVariable Long userId) {
        List<Category> categories = categoryService.getCustomCategories(userId);
        return ResponseEntity.ok(categories);
    }
    
    // Delete a category
    @DeleteMapping("/{categoryId}/user/{userId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId, @PathVariable Long userId) {
       
            categoryService.deleteCategory(categoryId, userId);
            return ResponseEntity.ok("Category deleted");
       
    }
}