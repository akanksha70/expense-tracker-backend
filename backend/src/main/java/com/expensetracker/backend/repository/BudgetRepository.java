package com.expensetracker.backend.repository;

import com.expensetracker.backend.entity.Budget;
import com.expensetracker.backend.entity.Budget.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    // Get all budgets for a user
    List<Budget> findByUserId(Long userId);
    
    // Get budget by type for a user
    List<Budget> findByUserIdAndBudgetType(Long userId, BudgetType budgetType);
    
    // Get active budget (current date is between start and end date)
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.budgetType = :budgetType " +
           "AND :currentDate BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findActiveBudgetByType(@Param("userId") Long userId, 
                                           @Param("budgetType") BudgetType budgetType,
                                           @Param("currentDate") LocalDateTime currentDate);
    
    // Get active category budget
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.category.id = :categoryId " +
           "AND :currentDate BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findActiveCategoryBudget(@Param("userId") Long userId,
                                             @Param("categoryId") Long categoryId,
                                             @Param("currentDate") LocalDateTime currentDate);
    
    // Get all active budgets for a user
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId " +
           "AND :currentDate BETWEEN b.startDate AND b.endDate")
    List<Budget> findAllActiveBudgets(@Param("userId") Long userId,
                                     @Param("currentDate") LocalDateTime currentDate);
    
    // Check if budget exists for user, type, and date range
    @Query("SELECT COUNT(b) > 0 FROM Budget b WHERE b.user.id = :userId " +
           "AND b.budgetType = :budgetType AND b.category.id IS NULL " +
           "AND ((b.startDate BETWEEN :startDate AND :endDate) " +
           "OR (b.endDate BETWEEN :startDate AND :endDate))")
    boolean existsOverlappingBudget(@Param("userId") Long userId,
                                   @Param("budgetType") BudgetType budgetType,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);
}