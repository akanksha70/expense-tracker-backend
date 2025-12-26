package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.StreakInfo;
import com.expensetracker.backend.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {
    
    @Autowired
    private StreakService streakService;
    
    // Get expense logging streak
    @GetMapping("/{userId}/logging")
    public ResponseEntity<StreakInfo> getLoggingStreak(@PathVariable Long userId) {
        StreakInfo streak = streakService.getLoggingStreak(userId);
        return ResponseEntity.ok(streak);
    }
    
    // Get no-spend streak
    @GetMapping("/{userId}/no-spend")
    public ResponseEntity<StreakInfo> getNoSpendStreak(@PathVariable Long userId) {
        StreakInfo streak = streakService.getNoSpendStreak(userId);
        return ResponseEntity.ok(streak);
    }
}