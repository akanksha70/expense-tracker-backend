# Project Structure
```
expense-tracker-backend/
│
├── src/
│   ├── main/
│   │   ├── java/com/expensetracker/backend/
│   │   │   ├── config/
│   │   │   │   ├── JacksonConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── BudgetController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ExpenseController.java
│   │   │   │   ├── InsightsController.java
│   │   │   │   ├── StreakController.java
│   │   │   │   └── UserController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── BudgetResponse.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   ├── SpendingInsights.java
│   │   │   │   ├── StreakInfo.java
│   │   │   │   └── UserResponse.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── Budget.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Expense.java
│   │   │   │   └── User.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidOperationException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── UnauthorizedAccessException.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── BudgetRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── ExpenseRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── BudgetService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── ExpenseService.java
│   │   │   │   ├── InsightsService.java
│   │   │   │   ├── StreakService.java
│   │   │   │   └── UserService.java
│   │   │   │
│   │   │   └── BackendApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties (gitignored)
│   │
│   └── test/ (unit tests can be added)
│
├── .gitignore
├── pom.xml
├── README.md
├── API_TESTING.md
└── PROJECT_STRUCTURE.md
```

## Layer Responsibilities

### 1. Entity Layer
- Database table mappings
- Field validations
- Relationships

### 2. Repository Layer
- Database operations
- Custom queries
- Spring Data JPA methods

### 3. Service Layer
- Business logic
- Data processing
- Exception handling

### 4. Controller Layer
- HTTP endpoints
- Request/response handling
- API documentation

### 5. DTO Layer
- Data transfer objects
- Response formatting
- Hide sensitive data

### 6. Exception Layer
- Custom exceptions
- Global error handling
- Consistent error responses
