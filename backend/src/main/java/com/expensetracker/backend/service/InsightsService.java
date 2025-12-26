package com.expensetracker.backend.service;

import com.expensetracker.backend.dto.SpendingInsights;
import com.expensetracker.backend.entity.Expense;
import com.expensetracker.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsightsService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    // Get comprehensive spending insights
    public SpendingInsights getSpendingInsights(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
        
        SpendingInsights insights = new SpendingInsights();
        
        if (expenses.isEmpty()) {
            insights.setTotalSpending(BigDecimal.ZERO);
            insights.setTotalTransactions(0);
            insights.setCategoryBreakdown(new HashMap<>());
            insights.setEmotionBreakdown(new HashMap<>());
            insights.setDailySpending(new HashMap<>());
            insights.setHourlySpending(new HashMap<>());
            insights.setAverageDailySpending(BigDecimal.ZERO);
            return insights;
        }
        
        // Total spending
        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        insights.setTotalSpending(total);
        insights.setTotalTransactions(expenses.size());
        
        // Category breakdown
        Map<String, BigDecimal> categoryBreakdown = expenses.stream()
                .collect(Collectors.groupingBy(
                    e -> e.getCategory().getName(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        insights.setCategoryBreakdown(categoryBreakdown);
        
        // Top category
        String topCategory = categoryBreakdown.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        insights.setTopCategory(topCategory);
        
        // Emotion breakdown
        Map<String, BigDecimal> emotionBreakdown = expenses.stream()
                .filter(e -> e.getEmotion() != null && !e.getEmotion().isEmpty())
                .collect(Collectors.groupingBy(
                    Expense::getEmotion,
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        insights.setEmotionBreakdown(emotionBreakdown);
        
        // Top emotion
        String topEmotion = emotionBreakdown.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        insights.setTopEmotion(topEmotion);
        
        // Daily spending
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, BigDecimal> dailySpending = expenses.stream()
                .collect(Collectors.groupingBy(
                    e -> e.getExpenseDate().format(dayFormatter),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        insights.setDailySpending(dailySpending);
        
        // Hourly spending (hour of day pattern)
        Map<Integer, BigDecimal> hourlySpending = expenses.stream()
                .collect(Collectors.groupingBy(
                    e -> e.getExpenseDate().getHour(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        insights.setHourlySpending(hourlySpending);
        
        // Average daily spending
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal avgDaily = total.divide(BigDecimal.valueOf(daysBetween), 2, RoundingMode.HALF_UP);
        insights.setAverageDailySpending(avgDaily);
        
        return insights;
    }
    
    // Get category-wise spending comparison
    public Map<String, Object> getCategoryComparison(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
        
        Map<String, BigDecimal> spending = expenses.stream()
                .collect(Collectors.groupingBy(
                    e -> e.getCategory().getName(),
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        
        BigDecimal total = spending.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> result = new HashMap<>();
        result.put("categorySpending", spending);
        result.put("totalSpending", total);
        
        // Calculate percentages
        Map<String, Double> percentages = new HashMap<>();
        spending.forEach((category, amount) -> {
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                double percentage = amount.divide(total, 4, RoundingMode.HALF_UP)
                                         .multiply(BigDecimal.valueOf(100))
                                         .doubleValue();
                percentages.put(category, percentage);
            }
        });
        result.put("categoryPercentages", percentages);
        
        return result;
    }
    
    // Get emotion vs spending analysis
    public Map<String, Object> getEmotionAnalysis(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate)
                .stream()
                .filter(e -> e.getEmotion() != null && !e.getEmotion().isEmpty())
                .collect(Collectors.toList());
        
        Map<String, BigDecimal> emotionSpending = expenses.stream()
                .collect(Collectors.groupingBy(
                    Expense::getEmotion,
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
        
        Map<String, Long> emotionCount = expenses.stream()
                .collect(Collectors.groupingBy(
                    Expense::getEmotion,
                    Collectors.counting()
                ));
        
        Map<String, BigDecimal> emotionAverage = new HashMap<>();
        emotionSpending.forEach((emotion, total) -> {
            Long count = emotionCount.get(emotion);
            BigDecimal avg = total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
            emotionAverage.put(emotion, avg);
        });
        
        Map<String, Object> result = new HashMap<>();
        result.put("emotionSpending", emotionSpending);
        result.put("emotionCount", emotionCount);
        result.put("emotionAverage", emotionAverage);
        
        return result;
    }
    
    // Get spending trend (day-wise)
    public Map<String, BigDecimal> getSpendingTrend(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        
        List<Expense> expenses = expenseRepository.findByUserIdAndExpenseDateBetween(userId, startDate, endDate);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        return expenses.stream()
                .collect(Collectors.groupingBy(
                    e -> e.getExpenseDate().format(formatter),
                    TreeMap::new,
                    Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
    }
}