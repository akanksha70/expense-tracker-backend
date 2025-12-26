package com.expensetracker.backend.service;

import com.expensetracker.backend.entity.Category;
import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.exception.DuplicateResourceException;
import com.expensetracker.backend.exception.InvalidOperationException;
import com.expensetracker.backend.exception.ResourceNotFoundException;
import com.expensetracker.backend.exception.UnauthorizedAccessException;
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
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
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
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Check if category with same name exists for this user
        Category existing = categoryRepository.findByNameAndUserId(name, userId);
        if (existing != null) {
            throw new DuplicateResourceException("Category", "name", name);
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
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        // Check if category belongs to user
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Category", categoryId, userId);
        }
        
        // Don't allow deleting default categories
        if (category.getIsDefault()) {
            throw new InvalidOperationException("Cannot delete default category");
        }
        
        categoryRepository.delete(category);
    }
}