package com.akrisht.expenseTracker.controller;

import com.akrisht.expenseTracker.entity.Expense;
import com.akrisht.expenseTracker.repository.UserRepository;
import com.akrisht.expenseTracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createExpense(@RequestBody Expense expense) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            Expense createdExpense = expenseService.createExpense(userId, expense);
            return new ResponseEntity<>("Expense added successful!",HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getExpenses() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            List<Expense> expenses = expenseService.getExpensesByUserId(userId);
            return ResponseEntity.ok(expenses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpense(@PathVariable Long id) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            Expense expense = expenseService.getExpenseById(id, userId);
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            Expense updatedExpense = expenseService.updateExpense(id, userId, expense);
            return ResponseEntity.ok(updatedExpense);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            expenseService.deleteExpense(id, userId);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalExpenses(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            Double total = expenseService.getTotalExpenses(userId, start, end);
            return ResponseEntity.ok("Total Expenses: " + total);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format or error: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategoryTotals() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userRepository.findByEmail(email).get().getId();
        try {
            Map<String, Double> categoryTotals = expenseService.getCategoryTotals(userId);
            return ResponseEntity.ok(categoryTotals);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}