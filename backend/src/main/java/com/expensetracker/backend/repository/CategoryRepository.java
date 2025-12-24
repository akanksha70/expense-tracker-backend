package com.expensetracker.backend.repository;

import com.expensetracker.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find all categories for a specific user
    List<Category> findByUserId(Long userId);
    
    // Find default categories for a user
    List<Category> findByUserIdAndIsDefault(Long userId, Boolean isDefault);
    
    // Find category by name and user
    Category findByNameAndUserId(String name, Long userId);
}