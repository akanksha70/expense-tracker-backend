package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.SpendingInsights;
import com.expensetracker.backend.service.InsightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/insights")
public class InsightsController {
    
    @Autowired
    private InsightsService insightsService;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    // Get comprehensive insights
    @GetMapping("/{userId}")
    public ResponseEntity<SpendingInsights> getInsights(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        SpendingInsights insights = insightsService.getSpendingInsights(userId, start, end);
        return ResponseEntity.ok(insights);
    }
    
    // Get category comparison
    @GetMapping("/{userId}/categories")
    public ResponseEntity<Map<String, Object>> getCategoryComparison(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        Map<String, Object> comparison = insightsService.getCategoryComparison(userId, start, end);
        return ResponseEntity.ok(comparison);
    }
    
    // Get emotion analysis
    @GetMapping("/{userId}/emotions")
    public ResponseEntity<Map<String, Object>> getEmotionAnalysis(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        Map<String, Object> analysis = insightsService.getEmotionAnalysis(userId, start, end);
        return ResponseEntity.ok(analysis);
    }
    
    // Get spending trend
    @GetMapping("/{userId}/trend")
    public ResponseEntity<Map<String, BigDecimal>> getSpendingTrend(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        
        Map<String, BigDecimal> trend = insightsService.getSpendingTrend(userId, start, end);
        return ResponseEntity.ok(trend);
    }
}