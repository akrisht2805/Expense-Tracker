package com.akrisht.expenseTracker.repository;

import com.akrisht.expenseTracker.entity.Expense;
import com.akrisht.expenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
