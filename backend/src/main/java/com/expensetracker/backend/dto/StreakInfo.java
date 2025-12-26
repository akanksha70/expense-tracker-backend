package com.expensetracker.backend.dto;

import java.time.LocalDate;

public class StreakInfo {
    
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDate lastActivityDate;
    private String streakType;
    
    public StreakInfo() {
    }
    
    public StreakInfo(Integer currentStreak, Integer longestStreak, LocalDate lastActivityDate, String streakType) {
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastActivityDate = lastActivityDate;
        this.streakType = streakType;
    }
    
    // Getters and Setters
    public Integer getCurrentStreak() {
        return currentStreak;
    }
    
    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }
    
    public Integer getLongestStreak() {
        return longestStreak;
    }
    
    public void setLongestStreak(Integer longestStreak) {
        this.longestStreak = longestStreak;
    }
    
    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }
    
    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }
    
    public String getStreakType() {
        return streakType;
    }
    
    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }
}