package com.expensetracker.backend.service;

import com.expensetracker.backend.dto.BudgetResponse;
import com.expensetracker.backend.entity.Budget;
import com.expensetracker.backend.entity.Budget.BudgetType;
import com.expensetracker.backend.entity.Category;
import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.exception.DuplicateResourceException;
import com.expensetracker.backend.exception.InvalidOperationException;
import com.expensetracker.backend.exception.ResourceNotFoundException;
import com.expensetracker.backend.exception.UnauthorizedAccessException;
import com.expensetracker.backend.repository.BudgetRepository;
import com.expensetracker.backend.repository.CategoryRepository;
import com.expensetracker.backend.repository.ExpenseRepository;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    // Create overall budget (weekly or monthly)
    public Budget createOverallBudget(Long userId, BigDecimal amount, BudgetType budgetType, 
                                     LocalDateTime startDate, LocalDateTime endDate) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        // Validate budget type
        if (budgetType == BudgetType.CATEGORY) {
            throw new InvalidOperationException("Use createCategoryBudget for category-specific budgets");
        }
        
        // Check for overlapping budgets
        if (budgetRepository.existsOverlappingBudget(userId, budgetType, startDate, endDate)) {
            throw new DuplicateResourceException("Budget", "period", budgetType + " budget already exists for this period");
        }
        
        Budget budget = new Budget(user, null, amount, budgetType, startDate, endDate);
        return budgetRepository.save(budget);
    }
    
    // Create category-specific budget
    public Budget createCategoryBudget(Long userId, Long categoryId, BigDecimal amount, 
                                      LocalDateTime startDate, LocalDateTime endDate) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        // Verify category belongs to user
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("User is not authorized to use this category");
        }
        
        // Check if active category budget already exists
        Optional<Budget> existing = budgetRepository.findActiveCategoryBudget(userId, categoryId, LocalDateTime.now());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Budget", "category", "Active budget already exists for this category");
        }
        
        Budget budget = new Budget(user, category, amount, BudgetType.CATEGORY, startDate, endDate);
        return budgetRepository.save(budget);
    }
    
    // Get active budget with spending details
    public BudgetResponse getActiveBudgetDetails(Long userId, BudgetType budgetType) {
        
        Budget budget = budgetRepository.findActiveBudgetByType(userId, budgetType, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "type", budgetType));
        
        return buildBudgetResponse(budget);
    }
    
    // Get active category budget details
    public BudgetResponse getActiveCategoryBudgetDetails(Long userId, Long categoryId) {
        
        Budget budget = budgetRepository.findActiveCategoryBudget(userId, categoryId, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "category", categoryId));
        
        return buildBudgetResponse(budget);
    }
    
    // Get all active budgets for user
    public List<BudgetResponse> getAllActiveBudgets(Long userId) {
        List<Budget> budgets = budgetRepository.findAllActiveBudgets(userId, LocalDateTime.now());
        List<BudgetResponse> responses = new ArrayList<>();
        
        for (Budget budget : budgets) {
            responses.add(buildBudgetResponse(budget));
        }
        
        return responses;
    }
    
    // Update budget
    public Budget updateBudget(Long budgetId, Long userId, BigDecimal newAmount) {
        
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", budgetId));
        
        // Verify budget belongs to user
        if (!budget.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Budget", budgetId, userId);
        }
        
        budget.setAmount(newAmount);
        return budgetRepository.save(budget);
    }
    
    // Delete budget
    public void deleteBudget(Long budgetId, Long userId) {
        
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", budgetId));
        
        // Verify budget belongs to user
        if (!budget.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Budget", budgetId, userId);
        }
        
        budgetRepository.delete(budget);
    }
    
    // Helper method to build budget response with spending details
    private BudgetResponse buildBudgetResponse(Budget budget) {
        BudgetResponse response = new BudgetResponse();
        
        response.setId(budget.getId());
        response.setUserId(budget.getUser().getId());
        response.setBudgetAmount(budget.getAmount());
        response.setBudgetType(budget.getBudgetType());
        response.setStartDate(budget.getStartDate());
        response.setEndDate(budget.getEndDate());
        
        // Calculate spent amount
        Double spent;
        if (budget.getCategory() != null) {
            // Category-specific budget
            response.setCategoryId(budget.getCategory().getId());
            response.setCategoryName(budget.getCategory().getName());
            spent = expenseRepository.getTotalExpenseByCategoryAndUserId(
                budget.getUser().getId(), 
                budget.getCategory().getId()
            );
        } else {
            // Overall budget
            spent = expenseRepository.getTotalExpenseByUserId(budget.getUser().getId());
        }
        
        BigDecimal spentAmount = spent != null ? BigDecimal.valueOf(spent) : BigDecimal.ZERO;
        response.setSpentAmount(spentAmount);
        
        // Calculate remaining amount
        BigDecimal remaining = budget.getAmount().subtract(spentAmount);
        response.setRemainingAmount(remaining);
        
        // Calculate percentage used
        if (budget.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            double percentage = spentAmount.divide(budget.getAmount(), 4, RoundingMode.HALF_UP)
                                          .multiply(BigDecimal.valueOf(100))
                                          .doubleValue();
            response.setPercentageUsed(percentage);
        } else {
            response.setPercentageUsed(0.0);
        }
        
        // Check if exceeded
        response.setExceeded(spentAmount.compareTo(budget.getAmount()) > 0);
        
        return response;
    }
    
    // Get all budgets for user (not just active)
    public List<Budget> getAllBudgets(Long userId) {
        return budgetRepository.findByUserId(userId);
    }
}