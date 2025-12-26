# API Testing Guide

## Quick Test Flow

### 1. Register User
POST `/api/users/register`
```json
{
  "email": "demo@example.com",
  "password": "demo123"
}
```
**Save the returned `id`**

---

### 2. Login
POST `/api/users/login`
```json
{
  "email": "demo@example.com",
  "password": "demo123"
}
```

---

### 3. Create Default Categories
POST `/api/categories/default/1`

---

### 4. Add Expenses
POST `/api/expenses`
```json
{
  "userId": 1,
  "categoryId": 1,
  "amount": 250,
  "expenseDate": "2025-12-25T14:00:00",
  "note": "Lunch",
  "emotion": "happy"
}
```

Add 5-10 different expenses for testing.

---

### 5. Create Weekly Budget
POST `/api/budgets/overall`
```json
{
  "userId": 1,
  "amount": 5000,
  "budgetType": "WEEKLY",
  "startDate": "2025-12-23T00:00:00",
  "endDate": "2025-12-29T23:59:59"
}
```

---

### 6. Check Budget Status
GET `/api/budgets/active/1/WEEKLY`

---

### 7. Get Insights
GET `/api/insights/1?startDate=2025-12-01T00:00:00&endDate=2025-12-31T23:59:59`

---

### 8. Check Streaks
GET `/api/streaks/1/logging`

---

## Complete Postman Collection

Import this JSON into Postman:

**File:** `Expense_Tracker_APIs.postman_collection.json`
