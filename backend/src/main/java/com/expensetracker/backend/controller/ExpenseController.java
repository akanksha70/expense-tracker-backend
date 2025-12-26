package com.expensetracker.backend.controller;

import com.expensetracker.backend.entity.Expense;
import com.expensetracker.backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    // Add a new expense
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody Map<String, Object> request) {
       
            Long userId = Long.valueOf(request.get("userId").toString());
            Long categoryId = Long.valueOf(request.get("categoryId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            String expenseDateStr = (String) request.get("expenseDate");
            LocalDateTime expenseDate = expenseDateStr != null 
                ? LocalDateTime.parse(expenseDateStr, formatter) 
                : LocalDateTime.now();
            
            String note = (String) request.get("note");
            String emotion = (String) request.get("emotion");
            
            Expense expense = expenseService.addExpense(userId, categoryId, amount, expenseDate, note, emotion);
            return ResponseEntity.status(HttpStatus.CREATED).body(expense);
       
    }
    
    // Get all expenses for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getUserExpenses(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getUserExpenses(userId);
        return ResponseEntity.ok(expenses);
    }
    
    // Get expenses by category
    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(
            @PathVariable Long userId, 
            @PathVariable Long categoryId) {
        List<Expense> expenses = expenseService.getExpensesByCategory(userId, categoryId);
        return ResponseEntity.ok(expenses);
    }
    
    // Get expenses by date range
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<Expense> expenses = expenseService.getExpensesByDateRange(userId, start, end);
        return ResponseEntity.ok(expenses);
    }
    
    // Get expenses by emotion
    @GetMapping("/user/{userId}/emotion/{emotion}")
    public ResponseEntity<List<Expense>> getExpensesByEmotion(
            @PathVariable Long userId,
            @PathVariable String emotion) {
        List<Expense> expenses = expenseService.getExpensesByEmotion(userId, emotion);
        return ResponseEntity.ok(expenses);
    }
    
    // Get total expense for user
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalExpense(@PathVariable Long userId) {
        Double total = expenseService.getTotalExpense(userId);
        return ResponseEntity.ok(total);
    }
    
    // Get total expense by category
    @GetMapping("/user/{userId}/category/{categoryId}/total")
    public ResponseEntity<Double> getTotalExpenseByCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        Double total = expenseService.getTotalExpenseByCategory(userId, categoryId);
        return ResponseEntity.ok(total);
    }
    
    // Update an expense
    @PutMapping("/{expenseId}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody Map<String, Object> request) {
       
            Long userId = Long.valueOf(request.get("userId").toString());
            
            BigDecimal amount = request.containsKey("amount") 
                ? new BigDecimal(request.get("amount").toString()) 
                : null;
            
            Long categoryId = request.containsKey("categoryId") 
                ? Long.valueOf(request.get("categoryId").toString()) 
                : null;
            
            LocalDateTime expenseDate = request.containsKey("expenseDate") 
                ? LocalDateTime.parse((String) request.get("expenseDate"), formatter) 
                : null;
            
            String note = (String) request.get("note");
            String emotion = (String) request.get("emotion");
            
            Expense expense = expenseService.updateExpense(
                expenseId, userId, amount, categoryId, expenseDate, note, emotion
            );
            return ResponseEntity.ok(expense);
       
    }
    
    // Delete an expense
    @DeleteMapping("/{expenseId}/user/{userId}")
    public ResponseEntity<?> deleteExpense(
            @PathVariable Long expenseId,
            @PathVariable Long userId) {
       
            expenseService.deleteExpense(expenseId, userId);
            return ResponseEntity.ok("Expense deleted");
        
    }
}


