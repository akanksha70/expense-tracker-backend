package com.expensetracker.backend.repository;

import com.expensetracker.backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    // Find all expenses for a user
    List<Expense> findByUserId(Long userId);
    
    // Find expenses by user and category
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
    
    // Find expenses within a date range
    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find expenses by emotion
    List<Expense> findByUserIdAndEmotion(Long userId, String emotion);
    
    // Get total expense amount for a user (custom query)
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double getTotalExpenseByUserId(@Param("userId") Long userId);
    
    // Get total expense by category
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.category.id = :categoryId")
    Double getTotalExpenseByCategoryAndUserId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
}