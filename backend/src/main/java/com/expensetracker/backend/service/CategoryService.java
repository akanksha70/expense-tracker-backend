package com.expensetracker.backend.service;

import com.expensetracker.backend.entity.Category;
import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.repository.CategoryRepository;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create default categories for a new user
    public void createDefaultCategories(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String[] defaultCategoryNames = {
            "Food", "Travel", "Pets", "Apparel", "Transport", 
            "Culture", "Education", "Gift", "Healthcare", "Entertainment"
        };
        
        for (String name : defaultCategoryNames) {
            Category category = new Category(name, true, user);
            categoryRepository.save(category);
        }
    }
    
    // Create custom category
    public Category createCustomCategory(String name, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if category with same name exists for this user
        Category existing = categoryRepository.findByNameAndUserId(name, userId);
        if (existing != null) {
            throw new RuntimeException("Category already exists");
        }
        
        Category category = new Category(name, false, user);
        return categoryRepository.save(category);
    }
    
    // Get all categories for a user
    public List<Category> getUserCategories(Long userId) {
        return categoryRepository.findByUserId(userId);
    }
    
    // Get only default categories
    public List<Category> getDefaultCategories(Long userId) {
        return categoryRepository.findByUserIdAndIsDefault(userId, true);
    }
    
    // Get only custom categories
    public List<Category> getCustomCategories(Long userId) {
        return categoryRepository.findByUserIdAndIsDefault(userId, false);
    }
    
    // Delete a custom category
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Check if category belongs to user
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        
        // Don't allow deleting default categories
        if (category.getIsDefault()) {
            throw new RuntimeException("Cannot delete default category");
        }
        
        categoryRepository.delete(category);
    }
}