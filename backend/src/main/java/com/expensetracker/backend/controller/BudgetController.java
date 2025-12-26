package com.expensetracker.backend.controller;

import com.expensetracker.backend.dto.BudgetResponse;
import com.expensetracker.backend.entity.Budget;
import com.expensetracker.backend.entity.Budget.BudgetType;
import com.expensetracker.backend.service.BudgetService;
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
@RequestMapping("/api/budgets")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    // Create overall budget (weekly or monthly)
    @PostMapping("/overall")
    public ResponseEntity<?> createOverallBudget(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        BudgetType budgetType = BudgetType.valueOf(request.get("budgetType").toString());
        LocalDateTime startDate = LocalDateTime.parse((String) request.get("startDate"), formatter);
        LocalDateTime endDate = LocalDateTime.parse((String) request.get("endDate"), formatter);
        
        Budget budget = budgetService.createOverallBudget(userId, amount, budgetType, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(budget);
    }
    
    // Create category budget
    @PostMapping("/category")
    public ResponseEntity<?> createCategoryBudget(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long categoryId = Long.valueOf(request.get("categoryId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        LocalDateTime startDate = LocalDateTime.parse((String) request.get("startDate"), formatter);
        LocalDateTime endDate = LocalDateTime.parse((String) request.get("endDate"), formatter);
        
        Budget budget = budgetService.createCategoryBudget(userId, categoryId, amount, startDate, endDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(budget);
    }
    
    // Get active budget details by type
    @GetMapping("/active/{userId}/{budgetType}")
    public ResponseEntity<BudgetResponse> getActiveBudget(
            @PathVariable Long userId,
            @PathVariable String budgetType) {
        
        BudgetType type = BudgetType.valueOf(budgetType.toUpperCase());
        BudgetResponse response = budgetService.getActiveBudgetDetails(userId, type);
        return ResponseEntity.ok(response);
    }
    
    // Get active category budget details
    @GetMapping("/active/{userId}/category/{categoryId}")
    public ResponseEntity<BudgetResponse> getActiveCategoryBudget(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        
        BudgetResponse response = budgetService.getActiveCategoryBudgetDetails(userId, categoryId);
        return ResponseEntity.ok(response);
    }
    
    // Get all active budgets for user
    @GetMapping("/active/{userId}")
    public ResponseEntity<List<BudgetResponse>> getAllActiveBudgets(@PathVariable Long userId) {
        List<BudgetResponse> responses = budgetService.getAllActiveBudgets(userId);
        return ResponseEntity.ok(responses);
    }
    
    // Get all budgets for user (including inactive)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Budget>> getAllBudgets(@PathVariable Long userId) {
        List<Budget> budgets = budgetService.getAllBudgets(userId);
        return ResponseEntity.ok(budgets);
    }
    
    // Update budget amount
    @PutMapping("/{budgetId}")
    public ResponseEntity<?> updateBudget(
            @PathVariable Long budgetId,
            @RequestBody Map<String, Object> request) {
        
        Long userId = Long.valueOf(request.get("userId").toString());
        BigDecimal newAmount = new BigDecimal(request.get("amount").toString());
        
        Budget budget = budgetService.updateBudget(budgetId, userId, newAmount);
        return ResponseEntity.ok(budget);
    }
    
    // Delete budget
    @DeleteMapping("/{budgetId}/user/{userId}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable Long budgetId,
            @PathVariable Long userId) {
        
        budgetService.deleteBudget(budgetId, userId);
        return ResponseEntity.ok("Budget deleted successfully");
    }
}