package com.expensetracker.backend.service;

import com.expensetracker.backend.entity.Category;
import com.expensetracker.backend.entity.Expense;
import com.expensetracker.backend.entity.User;
import com.expensetracker.backend.exception.ResourceNotFoundException;
import com.expensetracker.backend.exception.UnauthorizedAccessException;
import com.expensetracker.backend.repository.CategoryRepository;
import com.expensetracker.backend.repository.ExpenseRepository;
import com.expensetracker.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Add a new expense
    public Expense addExpense(Long userId, Long categoryId, BigDecimal amount, 
                             LocalDateTime expenseDate, String note, String emotion) {
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        // Verify category belongs to user
        if (!category.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("User is not authorized to use this category");
        }
        
        Expense expense = new Expense(amount, category, user, expenseDate);
        expense.setNote(note);
        expense.setEmotion(emotion);
        
        return expenseRepository.save(expense);
    }
    
    // Get all expenses for a user
    public List<Expense> getUserExpenses(Long userId) {
        return expenseRepository.findByUserId(userId);
    }
    
    // Get expenses by category
    public List<Expense> getExpensesByCategory(Long userId, Long categoryId) {
        return expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
    }
    
    // Get expenses in date range
    public List<Expense> getExpensesByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
    }
    
    // Get expenses by emotion
    public List<Expense> getExpensesByEmotion(Long userId, String emotion) {
        return expenseRepository.findByUserIdAndEmotion(userId, emotion);
    }
    
    // Get total expense for user
    public Double getTotalExpense(Long userId) {
        Double total = expenseRepository.getTotalExpenseByUserId(userId);
        return total != null ? total : 0.0;
    }
    
    // Get total expense by category
    public Double getTotalExpenseByCategory(Long userId, Long categoryId) {
        Double total = expenseRepository.getTotalExpenseByCategoryAndUserId(userId, categoryId);
        return total != null ? total : 0.0;
    }
    
    // Update an expense
    public Expense updateExpense(Long expenseId, Long userId, BigDecimal amount, 
                                Long categoryId, LocalDateTime expenseDate, 
                                String note, String emotion) {
        
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        
        // Verify expense belongs to user
        if (!expense.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Expense", expenseId, userId);
        }
        
        if (amount != null) expense.setAmount(amount);
        
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
            
            if (!category.getUser().getId().equals(userId)) {
                throw new UnauthorizedAccessException("User is not authorized to use this category");
            }
            expense.setCategory(category);
        }
        
        if (expenseDate != null) expense.setExpenseDate(expenseDate);
        if (note != null) expense.setNote(note);
        if (emotion != null) expense.setEmotion(emotion);
        
        return expenseRepository.save(expense);
    }
    
    // Delete an expense
    public void deleteExpense(Long expenseId, Long userId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", expenseId));
        
        // Verify expense belongs to user
        if (!expense.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Expense", expenseId, userId);
        }
        
        expenseRepository.delete(expense);
    }
}