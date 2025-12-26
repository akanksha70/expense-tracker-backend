package com.expensetracker.backend.service;

import com.expensetracker.backend.dto.StreakInfo;
import com.expensetracker.backend.entity.Expense;
import com.expensetracker.backend.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class StreakService {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    // Get expense logging streak (consecutive days with at least one expense)
    public StreakInfo getLoggingStreak(Long userId) {
        
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        
        if (expenses.isEmpty()) {
            return new StreakInfo(0, 0, null, "LOGGING");
        }
        
        // Get unique dates (sorted)
        Set<LocalDate> expenseDates = expenses.stream()
                .map(e -> e.getExpenseDate().toLocalDate())
                .collect(Collectors.toCollection(TreeSet::new));
        
        LocalDate today = LocalDate.now();
        LocalDate lastDate = expenseDates.stream()
                .max(LocalDate::compareTo)
                .orElse(null);
        
        // Calculate current streak
        int currentStreak = 0;
        LocalDate checkDate = today;
        
        // Check if there's an expense today or yesterday
        if (expenseDates.contains(today)) {
            currentStreak = 1;
            checkDate = today.minusDays(1);
        } else if (expenseDates.contains(today.minusDays(1))) {
            currentStreak = 1;
            checkDate = today.minusDays(2);
        } else {
            // Streak broken
            return calculateLongestStreak(expenseDates, 0, lastDate);
        }
        
        // Count consecutive days backwards
        while (expenseDates.contains(checkDate)) {
            currentStreak++;
            checkDate = checkDate.minusDays(1);
        }
        
        // Calculate longest streak
        int longestStreak = calculateLongestStreakCount(expenseDates);
        longestStreak = Math.max(longestStreak, currentStreak);
        
        return new StreakInfo(currentStreak, longestStreak, lastDate, "LOGGING");
    }
    
    // Get no-spend streak (consecutive days without expenses)
    public StreakInfo getNoSpendStreak(Long userId) {
        
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        
        if (expenses.isEmpty()) {
            return new StreakInfo(0, 0, null, "NO_SPEND");
        }
        
        Set<LocalDate> expenseDates = expenses.stream()
                .map(e -> e.getExpenseDate().toLocalDate())
                .collect(Collectors.toCollection(TreeSet::new));
        
        LocalDate today = LocalDate.now();
        LocalDate lastExpenseDate = expenseDates.stream()
                .max(LocalDate::compareTo)
                .orElse(null);
        
        // Current no-spend streak
        int currentStreak = 0;
        LocalDate checkDate = today;
        
        while (!expenseDates.contains(checkDate)) {
            currentStreak++;
            checkDate = checkDate.minusDays(1);
            
            // Limit to reasonable history (e.g., 1 year)
            if (currentStreak > 365) break;
        }
        
        // Calculate longest no-spend streak
        int longestStreak = calculateLongestNoSpendStreak(expenseDates);
        longestStreak = Math.max(longestStreak, currentStreak);
        
        return new StreakInfo(currentStreak, longestStreak, lastExpenseDate, "NO_SPEND");
    }
    
    // Helper: Calculate longest consecutive streak
    private int calculateLongestStreakCount(Set<LocalDate> dates) {
        if (dates.isEmpty()) return 0;
        
        int longest = 1;
        int current = 1;
        LocalDate previous = null;
        
        for (LocalDate date : dates) {
            if (previous != null && date.equals(previous.plusDays(1))) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
            previous = date;
        }
        
        return longest;
    }
    
    // Helper: Calculate longest no-spend streak
    private int calculateLongestNoSpendStreak(Set<LocalDate> expenseDates) {
        if (expenseDates.isEmpty()) return 0;
        
        List<LocalDate> sortedDates = expenseDates.stream()
                .sorted()
                .collect(Collectors.toList());
        
        int longestGap = 0;
        
        for (int i = 1; i < sortedDates.size(); i++) {
            long gap = java.time.temporal.ChronoUnit.DAYS.between(
                sortedDates.get(i - 1), 
                sortedDates.get(i)
            ) - 1;
            longestGap = Math.max(longestGap, (int) gap);
        }
        
        return longestGap;
    }
    
    // Helper for calculating with last date
    private StreakInfo calculateLongestStreak(Set<LocalDate> dates, int currentStreak, LocalDate lastDate) {
        int longest = calculateLongestStreakCount(dates);
        return new StreakInfo(currentStreak, longest, lastDate, "LOGGING");
    }
}