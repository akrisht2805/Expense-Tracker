package com.akrisht.expenseTracker.service;

import com.akrisht.expenseTracker.entity.Expense;
import com.akrisht.expenseTracker.entity.User;
import com.akrisht.expenseTracker.repository.ExpenseRepository;
import com.akrisht.expenseTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public Expense createExpense(Long userId, Expense expense) {
        if (expense.getAmount() == null || expense.getCategory() == null || expense.getCategory().isEmpty()) {
            throw new RuntimeException("Amount and category are required");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public Expense getExpenseById(Long id, Long userId) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return expense;
    }

    public Expense updateExpense(Long id, Long userId, Expense updatedExpense) {
        Expense expense = getExpenseById(id, userId);
        if (updatedExpense.getAmount() == null || updatedExpense.getCategory() == null || updatedExpense.getCategory().isEmpty()) {
            throw new RuntimeException("Amount and category are required");
        }
        expense.setAmount(updatedExpense.getAmount());
        expense.setDescription(updatedExpense.getDescription());
        expense.setCategory(updatedExpense.getCategory());
        expense.setDate(updatedExpense.getDate());
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id, Long userId) {
        Expense expense = getExpenseById(id, userId);
        expenseRepository.delete(expense);
    }

    public Double getTotalExpenses(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public Map<String, Double> getCategoryTotals(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));
    }

    public Map<String, Object> getMonthlyReport(Long userId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        Double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));
        Map<String, Object> report = new HashMap<>();
        report.put("total", total);
        report.put("categories", categoryTotals);
        return report;
    }
}

