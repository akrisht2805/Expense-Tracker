package com.akrisht.expenseTracker.repository;

import com.akrisht.expenseTracker.entity.Expense;
import com.akrisht.expenseTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
