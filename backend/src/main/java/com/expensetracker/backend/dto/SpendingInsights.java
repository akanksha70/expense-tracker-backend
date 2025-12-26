package com.expensetracker.backend.dto;

import java.math.BigDecimal;
import java.util.Map;

public class SpendingInsights {
    
    private BigDecimal totalSpending;
    private Map<String, BigDecimal> categoryBreakdown;
    private Map<String, BigDecimal> emotionBreakdown;
    private Map<String, BigDecimal> dailySpending;
    private Map<Integer, BigDecimal> hourlySpending;
    private String topCategory;
    private String topEmotion;
    private BigDecimal averageDailySpending;
    private Integer totalTransactions;
    
    // Constructor
    public SpendingInsights() {
    }
    
    // Getters and Setters
    public BigDecimal getTotalSpending() {
        return totalSpending;
    }
    
    public void setTotalSpending(BigDecimal totalSpending) {
        this.totalSpending = totalSpending;
    }
    
    public Map<String, BigDecimal> getCategoryBreakdown() {
        return categoryBreakdown;
    }
    
    public void setCategoryBreakdown(Map<String, BigDecimal> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
    
    public Map<String, BigDecimal> getEmotionBreakdown() {
        return emotionBreakdown;
    }
    
    public void setEmotionBreakdown(Map<String, BigDecimal> emotionBreakdown) {
        this.emotionBreakdown = emotionBreakdown;
    }
    
    public Map<String, BigDecimal> getDailySpending() {
        return dailySpending;
    }
    
    public void setDailySpending(Map<String, BigDecimal> dailySpending) {
        this.dailySpending = dailySpending;
    }
    
    public Map<Integer, BigDecimal> getHourlySpending() {
        return hourlySpending;
    }
    
    public void setHourlySpending(Map<Integer, BigDecimal> hourlySpending) {
        this.hourlySpending = hourlySpending;
    }
    
    public String getTopCategory() {
        return topCategory;
    }
    
    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }
    
    public String getTopEmotion() {
        return topEmotion;
    }
    
    public void setTopEmotion(String topEmotion) {
        this.topEmotion = topEmotion;
    }
    
    public BigDecimal getAverageDailySpending() {
        return averageDailySpending;
    }
    
    public void setAverageDailySpending(BigDecimal averageDailySpending) {
        this.averageDailySpending = averageDailySpending;
    }
    
    public Integer getTotalTransactions() {
        return totalTransactions;
    }
    
    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}