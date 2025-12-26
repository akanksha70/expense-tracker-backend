package com.expensetracker.backend.dto;

import com.expensetracker.backend.entity.Budget.BudgetType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BudgetResponse {
    
    private Long id;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double percentageUsed;
    private BudgetType budgetType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isExceeded;
    
    // Constructor
    public BudgetResponse() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }
    
    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    
    public BigDecimal getSpentAmount() {
        return spentAmount;
    }
    
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }
    
    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }
    
    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
    
    public Double getPercentageUsed() {
        return percentageUsed;
    }
    
    public void setPercentageUsed(Double percentageUsed) {
        this.percentageUsed = percentageUsed;
    }
    
    public BudgetType getBudgetType() {
        return budgetType;
    }
    
    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public boolean isExceeded() {
        return isExceeded;
    }
    
    public void setExceeded(boolean exceeded) {
        isExceeded = exceeded;
    }
}